package cn.dark.demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @author dark
 * @date 2019-08-11
 */
public class Provider {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context
                = new ClassPathXmlApplicationContext("provider1.xml");
        context.start();
        System.in.read();
    }

}
