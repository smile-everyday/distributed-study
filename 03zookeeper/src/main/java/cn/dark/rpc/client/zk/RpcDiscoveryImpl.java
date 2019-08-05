package cn.dark.rpc.client.zk;

import cn.dark.rpc.ZkConnectUtils;
import cn.dark.rpc.client.zk.lb.ILB;
import cn.dark.rpc.client.zk.lb.RandomLB;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * 服务发现类
 *
 * @author dark
 * @date 2019-07-19
 */
public class RpcDiscoveryImpl implements IRpcDiscovery {

    private static final String SEPARATOR = "/";

    private CuratorFramework curator;
    private ILB lb; // 负载均衡器
    private List<String> serviceAddresses; // 服务地址

    public RpcDiscoveryImpl() {
        this(null);
    }

    public RpcDiscoveryImpl(ILB lb) {
        this.lb = lb;
        this.curator = ZkConnectUtils.getConnector();
    }

    /**
     * 发现服务
     *
     * @param serviceName
     * @return java.lang.String
     * @date 2019-07-19
     *
     */
    @Override
    public String discover(String serviceName) throws Exception {
        String node = SEPARATOR + serviceName;
        serviceAddresses = curator.getChildren().forPath(node);
        if (serviceAddresses == null || serviceAddresses.size() == 0) {
            throw new RuntimeException("未发现服务，无法进行远程调用！");
        }

        // 添加监听器，动态发现节点的变化
        addWatcher(node);

        // 可由外部配置负载均衡器，若未配置，则默认使用随机负载均衡器
        if (lb == null) {
            lb = new RandomLB();
        }
        return lb.selectHost(serviceAddresses);
    }

    private void addWatcher(String node) throws Exception {
        PathChildrenCache childrenCache = new PathChildrenCache(curator, node, true);
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                serviceAddresses = curator.getChildren().forPath(node);
            }
        };
        childrenCache.getListenable().addListener(listener);
        childrenCache.start();
    }

}
