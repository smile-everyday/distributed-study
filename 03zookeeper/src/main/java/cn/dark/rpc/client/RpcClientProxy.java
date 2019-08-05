package cn.dark.rpc.client;

import cn.dark.rpc.client.zk.IRpcDiscovery;

import java.lang.reflect.Proxy;

/**
 * @author dark
 * @date 2019-07-16
 */
public class RpcClientProxy {

    /**
     * 通过动态代理创建客户端代理对象
     *
     * @param interfaceClass 代理对象需实现的接口
     * @param discovery 发现服务
     * @return T
     * @date 2019-07-16
     *
     */
    public static <T> T newProxy(Class<T> interfaceClass, IRpcDiscovery discovery) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new RemoteHandler(discovery));
    }

}
