package cn.dark.rpc.client;

import cn.dark.rpc.server.RpcEntity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 远程调用handler，通过socket传输消息
 *
 * @author dark
 * @date 2019-07-16
 */
public class RemoteHandler implements InvocationHandler {

    private String host;
    private int port;

    public RemoteHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcEntity rpcEntity = new RpcEntity(method.getName(), args);
        SocketTransport transport = new SocketTransport(this.host, this.port);
        return transport.sendInfo(rpcEntity);
    }

}
