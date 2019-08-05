package cn.dark.rpc.client;

import cn.dark.rpc.client.zk.IRpcDiscovery;
import cn.dark.rpc.client.zk.RpcDiscoveryImpl;
import cn.dark.rpc.client.zk.lb.PollingLB;
import cn.dark.rpc.server.IHelloService;

/**
 * @author dark
 * @date 2019-07-16
 */
public class Client {

    public static void main(String[] args) {
        IRpcDiscovery discovery = new RpcDiscoveryImpl(new PollingLB());
        IHelloService iHelloService = RpcClientProxy.newProxy(IHelloService.class, discovery);
        for (int i = 0; i < 10; i++) {
            String result = iHelloService.sayHello("dark");
            System.out.println(result);
        }
    }

}
