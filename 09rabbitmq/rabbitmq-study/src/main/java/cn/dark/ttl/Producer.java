package cn.dark.ttl;

import cn.dark.utils.ConnectionUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author dark
 * @date 2019-11-08
 */
public class Producer {

    private static final String QUEUE_NAME = "ttl queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = ConnectionUtils.getConnection("192.168.0.106", 5672, "lwj", "lwj");
             Channel channel = connection.createChannel()) {
            // 设置队列中所有消息的过期时间，不是队列
            // 当队列和消息同时设置过期时间时，以小的为准
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-message-ttl", 20000);
            channel.queueDeclare(QUEUE_NAME, false, false, false, arguments);

            // 消息属性
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2) // 持久化
                    .contentType("utf8")
                    .expiration("5000") // 过期时间 5s
                    .build();
            // 发送到直连类型的amqp default exchange，只要有队列名字和routing key相同就能收到消息
            channel.basicPublish("", QUEUE_NAME, properties, "Hello, world!".getBytes());
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
