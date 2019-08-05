package cn.dark.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author dark
 * @date 2019-07-04
 */
public interface IHelloService extends Remote {

    String sayHello(String msg) throws RemoteException;

}
