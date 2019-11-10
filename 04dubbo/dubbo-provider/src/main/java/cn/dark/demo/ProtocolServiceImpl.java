package cn.dark.demo;

import cn.dark.api.IProtocolService;

/**
 * @author dark
 * @date 2019-08-11
 */
public class ProtocolServiceImpl implements IProtocolService {

    @Override
    public String sayHello(String s) {
        return "多协议演示，使用协议：" + s;
    }

}
