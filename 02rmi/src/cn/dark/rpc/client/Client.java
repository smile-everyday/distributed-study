package cn.dark.rpc.client;

import cn.dark.rpc.server.IHelloService;

/**
 * @author dark
 * @date 2019-07-16
 */
public class Client {

    public static void main(String[] args) {
        IHelloService iHelloService = RpcClientProxy.newProxy(IHelloService.class, "localhost", 8090);
        String result = iHelloService.sayHello("dark");
        System.out.println(result);
    }

}
