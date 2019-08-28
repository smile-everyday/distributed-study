package cn.dark.demo.cluster;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @author dark
 * @date 2019-08-11
 */
public class Cluster2 {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context
                = new ClassPathXmlApplicationContext("cluster/cluster2.xml");
        context.start();
        System.in.read();
    }

}
