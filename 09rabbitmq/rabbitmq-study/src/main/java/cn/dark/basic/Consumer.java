package cn.dark.basic;

import cn.dark.utils.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author dark
 * @date 2019-11-08
 */
public class Consumer {

    private static final String QUEUE_NAME = "myQueue";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 这里最好不要使用try-with语法，在异步接收消息情况下会导致连接关闭
        Connection connection = ConnectionUtils.getConnection("192.168.0.106", 5672, "lwj", "lwj");
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        System.out.println("waitting msg......");
        // DefaultConsumer consumer = new DefaultConsumer(channel) {
        //     @Override
        //     public void handleDelivery(String consumerTag, Envelope envelope,
        //                                AMQP.BasicProperties properties, byte[] body) throws IOException {
        //         String msg = new String(body, "utf8");
        //         System.out.println("receive msg: " + msg);
        //     }
        // };
        DeliverCallback callback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), "utf8");
            System.out.println("receive msg: " + msg);
        };
        channel.basicConsume(QUEUE_NAME, true, callback, consumerTag -> {});
    }

}
