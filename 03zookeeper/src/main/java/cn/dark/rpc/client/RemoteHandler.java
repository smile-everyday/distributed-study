package cn.dark.rpc.client;

import cn.dark.rpc.client.zk.IRpcDiscovery;
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

    private IRpcDiscovery discovery;

    public RemoteHandler(IRpcDiscovery discovery) {
        this.discovery = discovery;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String className = method.getDeclaringClass().getName();
        String address = discovery.discover(className);
        String[] arrs = address.split(":");

        RpcEntity rpcEntity = new RpcEntity(className, method.getName(), args);
        SocketTransport transport = new SocketTransport(arrs[0], Integer.parseInt(arrs[1]));
        return transport.sendInfo(rpcEntity);
    }

}
