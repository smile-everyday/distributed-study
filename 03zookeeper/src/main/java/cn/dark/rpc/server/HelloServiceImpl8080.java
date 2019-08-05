package cn.dark.rpc.server;

import cn.dark.rpc.server.zk.Service;

/**
 * @author dark
 * @date 2019-07-16
 */
@Service(IHelloService.class)
public class HelloServiceImpl8080 implements IHelloService {

    @Override
    public String sayHello(String msg) {
        return "8080: Hello, " + msg;
    }
}
