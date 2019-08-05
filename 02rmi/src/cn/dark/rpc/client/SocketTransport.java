package cn.dark.rpc.client;

import cn.dark.rpc.server.RpcEntity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * socket传输
 *
 * @author dark
 * @date 2019-07-16
 */
public class SocketTransport {

    private String host;
    private int port;

    public SocketTransport(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 发送消息给服务端
     *
     * @param rpcEntity
     * @return java.lang.Object
     * @date 2019-07-16
     */
    public Object sendInfo(RpcEntity rpcEntity) throws Exception {
        Socket socket = null;
        try {
            socket = newSocket();
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(rpcEntity);
            oos.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Object result = ois.readObject();

            ois.close();
            oos.close();
            return result;
        } catch (Exception e) {
            throw new Exception("远程调用出错！");
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    private Socket newSocket() throws Exception {
        System.out.println("创建连接......");
        Socket socket;
        try {
            socket = new Socket(this.host, this.port);
            return socket;
        } catch (Exception e) {
            throw new Exception("连接建立失败！");
        }
    }

}
