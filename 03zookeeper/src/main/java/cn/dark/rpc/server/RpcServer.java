package cn.dark.rpc.server;

import cn.dark.rpc.server.zk.IRpcRegistryCenter;
import cn.dark.rpc.server.zk.ServiceRepository;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author dark
 * @date 2019-07-16
 */
public class RpcServer {

    private static final ExecutorService SERVICE_POOL = Executors.newCachedThreadPool();

    private IRpcRegistryCenter registryCenter; // 注册中心
    private String address; // 服务地址
    private Integer port; // 服务端口

    public RpcServer(IRpcRegistryCenter registryCenter, String address, Integer port) {
        this.registryCenter = registryCenter;
        this.address = address;
        this.port = port;
    }

    /**
     * 发布服务
     *
     * @date 2019-07-16
     */
    public void publish() throws Exception {
        // 将服务注册到zookeeper中
        Set<String> serviceNames = ServiceRepository.getServiceNames();
        for (String serviceName : serviceNames) {
            registryCenter.register(serviceName, this.address, this.port);
            System.out.println("成功发布服务：" + serviceName + " -> " + this.address + ":" + this.port);
        }

        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            // 监听服务并交由线程池处理
            Socket socket = serverSocket.accept();
            SERVICE_POOL.execute(new ProcessHandler(socket));
        }
    }

}
