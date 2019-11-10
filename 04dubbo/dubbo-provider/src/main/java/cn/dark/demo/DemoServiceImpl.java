package cn.dark.demo;

import cn.dark.api.IDemoService;

/**
 * 服务实现类
 *
 * @author dark
 * @date 2019-08-11
 */
public class DemoServiceImpl implements IDemoService {

    @Override
    public String sayHello(String s) {
        return "Hello, " + s;
    }

}
