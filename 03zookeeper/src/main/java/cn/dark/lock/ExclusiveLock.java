package cn.dark.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 分布式独享锁的实现
 *
 * @author dark
 * @date 2019-07-11
 */
public class ExclusiveLock implements Lock, Watcher {

    private ZooKeeper zooKeeper;

    private final static String LOCKS = "/locks"; // 根节点
    private String currentLock; // 当前正在竞争的锁节点
    private String waitLock; // 当前锁节点未获取到锁时，需要等待释放的锁

    private CountDownLatch countDownLatch; // 实现线程等待的计数器

    public ExclusiveLock() throws IOException, KeeperException, InterruptedException {
        zooKeeper = new ZooKeeper("192.168.0.106,192.168.0.108,192.168.0.109",
                5000, this);

        // 这里存在并发创建根节点问题，需要同步
        synchronized (ExclusiveLock.class) {
            Stat stat = zooKeeper.exists(LOCKS, false);
            if (stat == null) {
                zooKeeper.create(LOCKS, "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        }
    }

    @Override
    public void lock() {
        // 尝试获取锁
        if (tryLock()) {
            System.out.println(Thread.currentThread().getName() + "->" + currentLock + "：成功获取到锁！");
            return;
        }

        try {
            // 未获取到锁时即等待上一个锁释放
            waitLock(waitLock);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean tryLock() {
        try {
            // 创建有序节点，设置为当前的锁节点
            currentLock = zooKeeper.create(LOCKS + "/", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            // 获取根节点下的所有子节点
            System.out.println(Thread.currentThread().getName() + "->" + currentLock + "：开始竞争锁！");
            List<String> children = zooKeeper.getChildren(LOCKS, false);
            TreeSet<String> treeSet = new TreeSet<>();
            for (String child : children) {
                treeSet.add(LOCKS + "/" + child);
            }

            // 取出有序节点中最小的一个即为第一个锁，因此该方式是公平锁
            String minNode = treeSet.first();
            if (currentLock.equals(minNode)) {
                return true;
            }

            // 未获取到锁时，当前线程需要等待前一个锁释放，即监听当前节点的前一个节点的变化
            SortedSet<String> headSet = treeSet.headSet(currentLock);
            if (!headSet.isEmpty()) {
                waitLock = headSet.last();
            }
            return false;
        } catch (KeeperException | InterruptedException e) {
            return false;
        }
    }

    private void waitLock(String prev) throws KeeperException, InterruptedException {
        // 监听前一个锁节点的变化（删除），并进入等待释放的状态
        // 这里存在一个问题，有可能判断时，前一个锁已经执行了unlock操作，即删除了该节点，
        // 那么这里的stat就会是null，因此就直接获取到锁
        Stat stat = zooKeeper.exists(prev, true);
        if (stat != null) {
            System.out.println(Thread.currentThread().getName() + " -> 等待锁释放：" + prev);
            countDownLatch = new CountDownLatch(1);
            countDownLatch.await();
        }
        System.out.println(Thread.currentThread().getName() + "->" + currentLock + "：成功获取到锁！");
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        try {
            System.out.println(Thread.currentThread().getName() + " -> 释放锁：" + currentLock);
            zooKeeper.delete(currentLock, -1);
            currentLock = null;
            zooKeeper.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        // 监听到锁节点变化（删除）后执行的操作，即获取到锁执行当前线程
        if (this.countDownLatch != null) {
            countDownLatch.countDown();
        }
    }
}
