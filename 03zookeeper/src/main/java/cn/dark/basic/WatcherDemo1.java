package cn.dark.basic;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author dark
 * @date 2019-07-09
 */
public class WatcherDemo1 {

    private static final String NODE = "/test";

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper("192.168.0.106,192.168.0.108,192.168.0.109",
                5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("默认事件：" + event.getType());
                if (Event.KeeperState.SyncConnected.equals(event.getState())) {
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();

        zooKeeper.create(NODE, "x".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        // exist/getData/getChildren可以绑定事件，但是都只会触发一次，即使嵌套绑定也只会触发相应次数的事件
        // 并且如果绑定时不是自定义water而是传入true，则是绑定全局的默认事件（构造函数设置的事件触发器）
        Stat stat = zooKeeper.exists(NODE, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event.getType() + "->" + event.getPath());
                try {
                    // 绑定的是全局事件
                    // zooKeeper.exists(event.getPath(), true);
                    // 绑定的是自定义事件
                    zooKeeper.exists(event.getPath(), new Watcher() {
                        @Override
                        public void process(WatchedEvent event) {
                            System.out.println(event.getType() + "->" + event.getPath());
                        }
                    });
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Stat stat1 = zooKeeper.setData(NODE, "xx".getBytes(), stat.getVersion());
        Stat stat2 = zooKeeper.setData(NODE, "xxx".getBytes(), stat1.getVersion());
        Stat stat3 = zooKeeper.setData(NODE, "xxx".getBytes(), stat2.getVersion());

        zooKeeper.delete(NODE, stat3.getVersion());
        zooKeeper.close();
    }

}
