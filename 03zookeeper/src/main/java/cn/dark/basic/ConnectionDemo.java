package cn.dark.basic;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author dark
 * @date 2019-07-08
 */
public class ConnectionDemo {

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper("192.168.0.106,192.168.0.108,192.168.0.109",
                5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                // 判断是否连接成功
                if (Event.KeeperState.SyncConnected.equals(event.getState())) {
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
        // 这里一定是CONNECTED状态
        System.out.println(zooKeeper.getState());

        zooKeeper.create("/test", "1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        // 节点属性，通过getData将属性存入
        Stat stat = new Stat();
        zooKeeper.getData("/test", null, stat);
        System.out.println(stat.getVersion());

        zooKeeper.setData("/test", "xx".getBytes(), stat.getVersion());
        byte[] data = zooKeeper.getData("/test", null, stat);
        System.out.println(new String(data));
        System.out.println(stat.getVersion());

        zooKeeper.delete("/test", stat.getVersion());

        zooKeeper.close();
    }

}
