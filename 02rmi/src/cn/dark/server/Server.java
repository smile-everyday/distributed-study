package cn.dark.server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * @author dark
 * @date 2019-07-04
 */
public class Server {

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, MalformedURLException {
        IHelloService helloService = new HelloServiceImpl();
        LocateRegistry.createRegistry(1099);
        Naming.rebind("rpc://127.0.0.1/hello", helloService);
        System.out.println("服务启动成功！");
    }

}
