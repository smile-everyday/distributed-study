package cn.dark.basic;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author dark
 * @date 2019-07-09
 */
public class WatcherDemo {

    private static final String NODE = "/test";

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper("192.168.0.106,192.168.0.108,192.168.0.109",
                5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("全局事件，初次绑定在连接成功后触发");
                if (Event.KeeperState.SyncConnected.equals(event.getState())) {
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();

        zooKeeper.create(NODE, "x".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        // 传入true则是再次绑定默认的监听器，所有监听器只会被触发一次，除非再次绑定
        Stat stat = zooKeeper.exists(NODE, true);
        // 触发刚刚绑定的事件
        zooKeeper.setData(NODE, "xx".getBytes(), stat.getVersion());
        byte[] data = zooKeeper.getData(NODE, false, stat);
        System.out.println(new String(data));

        // 3.5.5以后始终都会触发默认的事件
        zooKeeper.delete(NODE, stat.getVersion());
        zooKeeper.close();

    }

}
