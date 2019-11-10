package cn.dark.limitation;

import cn.dark.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author dark
 * @date 2019-11-08
 */
public class Consumer2 {

    private static final String QUEUE_NAME = "exchange queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection("192.168.0.106", 5672, "lwj", "lwj");
        Channel channel = connection.createChannel();

        DeliverCallback callback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), "utf8");
            System.out.println("receive msg: " + msg);
        };
        // 设置最大处理消息数，当达到该数量的消息未被确认时，不会接收新的消息
        // 注意有多个消费者时，也是轮询的方式分发消息
        channel.basicQos(3);
        channel.basicConsume(QUEUE_NAME, false, callback, consumerTag -> {});
    }

}
