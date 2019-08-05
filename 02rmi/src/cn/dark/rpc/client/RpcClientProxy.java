package cn.dark.rpc.client;

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
     * @param host ip地址
     * @param port 端口号
     * @return T
     * @date 2019-07-16
     *
     */
    public static <T> T newProxy(Class<T> interfaceClass, String host, int port) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new RemoteHandler(host, port));
    }

}
