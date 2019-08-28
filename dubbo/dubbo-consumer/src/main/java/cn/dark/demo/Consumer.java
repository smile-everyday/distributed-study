package cn.dark.demo;

import cn.dark.api.IDemoService;
import cn.dark.api.IProtocolService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author dark
 * @date 2019-08-11
 */
public class Consumer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context
                = new ClassPathXmlApplicationContext("consumer3.xml");
        context.start();

        IDemoService demoService = (IDemoService) context.getBean("demoService");
        System.out.println(demoService.sayHello("world"));
        IProtocolService protocolService = (IProtocolService) context.getBean("protocolService");
        System.out.println(protocolService.sayHello("hessian"));
    }

}
