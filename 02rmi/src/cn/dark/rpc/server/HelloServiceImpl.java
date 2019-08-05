package cn.dark.rpc.server;

/**
 * @author dark
 * @date 2019-07-16
 */
public class HelloServiceImpl implements IHelloService {

    @Override
    public String sayHello(String msg) {
        return "Hello, " + msg;
    }
}
