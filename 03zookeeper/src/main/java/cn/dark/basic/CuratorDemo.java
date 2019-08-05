package cn.dark.basic;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * @author dark
 * @date 2019-07-09
 */
public class CuratorDemo {

    public static void main(String[] args) throws Exception {
        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString("192.168.0.106,192.168.0.108,192.168.0.109")
                .sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("curator").build();
        curator.start();

        curator.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .forPath("/test/node1/node2", "1".getBytes());

        Stat stat = new Stat();
        curator.getData().storingStatIn(stat).forPath("/test/node1");
        curator.setData().withVersion(stat.getVersion()).forPath("/test/node1", "sas".getBytes());
        byte[] bytes = curator.getData().forPath("/test/node1");
        System.out.println(new String(bytes));

        curator.delete().deletingChildrenIfNeeded().forPath("/test");
        curator.close();
    }

}
