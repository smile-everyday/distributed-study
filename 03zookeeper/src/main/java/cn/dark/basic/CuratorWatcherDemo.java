package cn.dark.basic;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author dark
 * @date 2019-07-09
 */
public class CuratorWatcherDemo {

    public static void main(String[] args) throws Exception {
        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString("192.168.0.106,192.168.0.108,192.168.0.109")
                .sessionTimeoutMs(5000).retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("curator").build();
        curator.start();

        //NodeCache 监听节点的更新、创建事件
        // addListenerWithNodeCache(curator, "/test");

        // PathChildCache 监听节点的子节点的更新、创建、删除事件
        // addListenerWithPathChildCache(curator, "/test");

        // TreeCache 监听节点和子节点的更新、创建、删除事件
        addListenerWithTreeCache(curator, "/test");

        System.in.read();
    }

    private static void addListenerWithTreeCache(CuratorFramework curator, String node) throws Exception {
        TreeCache treeCache = new TreeCache(curator, node);
        TreeCacheListener listener = new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
                System.out.println("event:" + treeCacheEvent.getType());
            }
        };
        treeCache.getListenable().addListener(listener);
        treeCache.start();
    }

    private static void addListenerWithPathChildCache(CuratorFramework curator, String node) throws Exception {
        PathChildrenCache childrenCache = new PathChildrenCache(curator, node, true);
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                System.out.println("event:" + pathChildrenCacheEvent.getType());
            }
        };
        childrenCache.getListenable().addListener(listener);
        childrenCache.start();
    }

    private static void addListenerWithNodeCache(CuratorFramework curator, String node) throws Exception {
        final NodeCache nodeCache = new NodeCache(curator, node, false);
        NodeCacheListener listener = new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("event:" + nodeCache.getCurrentData().getPath());
            }
        };
        nodeCache.getListenable().addListener(listener);
        nodeCache.start();
    }

}
