package cn.dark.client;

import cn.dark.server.IHelloService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @author dark
 * @date 2019-07-04
 */
public class Client {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        IHelloService helloService = (IHelloService) Naming.lookup("rpc://127.0.0.1/hello");
        System.out.println(helloService.sayHello("lwj"));
    }

}
