package cn.dark.rpc.server;

import java.io.IOException;

/**
 * @author dark
 * @date 2019-07-16
 */
public class Server {

    public static void main(String[] args) throws IOException {
        IHelloService iHelloService = new HelloServiceImpl();
        RpcServer server = new RpcServer();
        server.publish(iHelloService, 8090);
    }

}
