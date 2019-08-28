package cn.dark.demo;

import cn.dark.api.IDemoService;
import cn.dark.api.IProtocolService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author dark
 * @date 2019-08-11
 */
public class Cluster {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context
                = new ClassPathXmlApplicationContext("cluster.xml");
        context.start();

        for (int i = 0; i < 10; i++) {
            IDemoService demoService = (IDemoService) context.getBean("demoService");
            System.out.println(demoService.sayHello("world"));
        }
    }

}
