package cn.dark.priority;

import cn.dark.utils.ConnectionUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author dark
 * @date 2019-11-08
 */
public class Consumer {

    private static final String QUEUE_NAME = "priority queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 这里最好不要使用try-with语法，在异步接收消息情况下会导致连接关闭
        Connection connection = ConnectionUtils.getConnection("192.168.0.106", 5672, "lwj", "lwj");
        Channel channel = connection.createChannel();

        System.out.println("waitting msg......");
        DeliverCallback callback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), "utf8");
            System.out.println("receive msg: " + msg);
        };
        channel.basicConsume(QUEUE_NAME, true, callback, consumerTag -> {});
    }

}
