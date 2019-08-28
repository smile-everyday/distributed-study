package cn.dark.demo.cluster;

import cn.dark.api.IDemoService;

/**
 * 服务实现类
 *
 * @author dark
 * @date 2019-08-11
 */
public class ClusterServiceImpl1 implements IDemoService {

    @Override
    public String sayHello(String s) {
        return "Hello, " + s + "! I'm server1";
    }

}
