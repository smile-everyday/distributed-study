package cn.dark.dlx;

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
public class Consumer2 {

    private static final String TEST_QUEUE_NAME = "test dlx queue with ttl";
    private static final String DLX_QUEUE_NAME = "dlx queue";

    private static final String TEST_EXCHANGE_NAME = "test dlx exchange with ttl";
    private static final String DLX_EXCHANGE_NAME = "dlx exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 这里最好不要使用try-with语法，在异步接收消息情况下会导致连接关闭
        Connection connection = ConnectionUtils.getConnection("192.168.0.106", 5672, "lwj", "lwj");
        Channel channel = connection.createChannel();
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 10000); // 过期的消息会直接进入私信队列
        arguments.put("x-dead-letter-exchange", DLX_EXCHANGE_NAME); // 将队列和死信队列进行绑定
        channel.exchangeDeclare(TEST_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(TEST_QUEUE_NAME, false, false, false, arguments);
        channel.queueBind(TEST_QUEUE_NAME, TEST_EXCHANGE_NAME, "ttl");

        // 创建死信队列及交换机
        channel.exchangeDeclare(DLX_EXCHANGE_NAME, BuiltinExchangeType.DIRECT, false, false, false, null);
        channel.queueDeclare(DLX_QUEUE_NAME, false, false, false, null);
        // 需要注意如果与该死信队列绑定的队列指定了routingKey，死信队列也需要指定所有需要接收的routingKey，否则会收不到消息
        channel.queueBind(DLX_QUEUE_NAME, DLX_EXCHANGE_NAME, "ttl");

        System.out.println("waitting msg......");
        DeliverCallback callback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), "utf8");
            System.out.println("receive msg: " + msg);
        };
        // 不消费等待消息过期进入死信队列
        // channel.basicConsume(TEST_QUEUE_NAME, false, callback, consumerTag -> {});
    }

}
