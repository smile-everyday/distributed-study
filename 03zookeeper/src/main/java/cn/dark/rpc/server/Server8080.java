package cn.dark.rpc.server;

import cn.dark.rpc.server.zk.IRpcRegistryCenter;
import cn.dark.rpc.server.zk.RpcRegistryCenterImpl;
import cn.dark.rpc.server.zk.ServiceRepository;

/**
 * @author dark
 * @date 2019-07-16
 */
public class Server8080 {

    public static void main(String[] args) throws Exception {
        // 绑定服务
        IHelloService iHelloService = new HelloServiceImpl8080();
        ServiceRepository.bind(iHelloService);

        // 将服务发布到注册中心
        IRpcRegistryCenter registryCenter = new RpcRegistryCenterImpl();
        RpcServer server = new RpcServer(registryCenter, "127.0.0.1", 8080);
        server.publish();
    }

}
