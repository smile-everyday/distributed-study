package cn.dark.rpc.client.zk;

/**
 * @author dark
 * @date 2019-07-19
 */
public interface IRpcDiscovery {

    String discover(String serviceName) throws Exception;

}
