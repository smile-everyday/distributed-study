package cn.dark.rpc.server.zk;

/**
 * RPC服务注册中心接口
 *
 * @author dark
 * @date 2019-07-18
 */
public interface IRpcRegistryCenter {

    void register(String serviceName, String address, Integer port) throws Exception;

}
