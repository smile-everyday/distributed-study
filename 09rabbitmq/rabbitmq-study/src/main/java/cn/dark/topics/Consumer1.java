package cn.dark.topics;

import cn.dark.utils.ConnectionUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author dark
 * @date 2019-11-08
 */
public class Consumer1 {

    private static final String EXCHANGE_NAME = "topic exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection("192.168.0.106", 5672, "lwj", "lwj");
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        // 通过该方式可以获取到临时的队列，该队列是non-durable、exclusive、autoDelete的
        // fanout类型交换机一般使用该类型的队列接收消息
        String queueName = channel.queueDeclare().getQueue();
        // 绑定队列和交换机
        // *代表只匹配一个单词，#代表任意个单词，单词以“.”分隔
        channel.queueBind(queueName, EXCHANGE_NAME, "*.black.*");

        DeliverCallback callback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), "utf8");
            System.out.println("receive msg: " + msg);
        };
        channel.basicConsume(queueName, true, callback, consumerTag -> {});
    }

}
