package cn.dark.rpc.server.zk;

import cn.dark.rpc.ZkConnectUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

/**
 *
 * @author dark
 * @date 2019-07-18
 */
public class RpcRegistryCenterImpl implements IRpcRegistryCenter {

    private static final String SEPARATOR = "/";

    /**
     * 服务注册，将服务的地址以临时节点的方式注册到zookeeper中，
     * 这样断开连接即销毁服务，而有新服务加入时，客户端通过监听器动态发现
     * 服务变化
     *
     * @param serviceName 服务名称
     * @param address     服务地址
     * @param port        服务端口
     * @return void
     * @date 2019-07-18
     */
    @Override
    public void register(String serviceName, String address, Integer port) throws Exception {
        String serviceAddress = address + ":" + port;
        String serviceNode = SEPARATOR + serviceName;

        CuratorFramework connector = ZkConnectUtils.getConnector();
        // 判断该服务节点是否已经创建，服务名称最好不要以临时节点的方式创建，否则一旦该连接断开，整个集群都将不可用。
        if (connector.checkExists().forPath(serviceNode) == null) {
            connector.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(serviceNode, "0".getBytes());
        }

        // 创建临时的地址节点
        String addressNode = serviceNode + SEPARATOR + serviceAddress;
        connector.create().withMode(CreateMode.EPHEMERAL).forPath(addressNode, "0".getBytes());
        System.out.println("服务注册成功！");
    }

}
