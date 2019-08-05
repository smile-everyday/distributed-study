package cn.dark.rpc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author dark
 * @date 2019-07-16
 */
public class RpcServer {

    private static final ExecutorService SERVICE_POOL = Executors.newCachedThreadPool();

    /**
     * 发布服务
     *
     * @param service 服务对象
     * @param port 端口号
     * @return void
     * @date 2019-07-16
     *
     */
    public void publish(Object service, int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            // 监听服务并交由线程池处理
            Socket socket = serverSocket.accept();
            SERVICE_POOL.execute(new ProcessHandler(socket, service));
        }
    }

}
