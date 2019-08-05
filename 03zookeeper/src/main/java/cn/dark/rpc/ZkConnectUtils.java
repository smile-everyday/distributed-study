package cn.dark.rpc;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * zookeeper连接类
 *
 * @author dark
 * @date 2019-07-18
 */
public class ZkConnectUtils {

    private static CuratorFramework curator;

    /**
     * 获取连接，默认连接地址，这里不能将创建连接写到静态块里初始化，
     * 否则服务端获取连接创建节点，客户端再去获取连接，因为是同一个
     * 对象，就会导致服务端连接断掉，从而创建的临时节点（服务地址）
     * 会被删除
     *
     * @return org.apache.curator.framework.CuratorFramework
     * @date 2019-07-18
     */
    public static CuratorFramework getConnector() {
        curator = CuratorFrameworkFactory.builder()
                .connectString(ZkConfig.CONNECT_STR)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace(ZkConfig.NAMESPACE)
                .sessionTimeoutMs(5000).build();
        curator.start();
        return curator;
    }

}
