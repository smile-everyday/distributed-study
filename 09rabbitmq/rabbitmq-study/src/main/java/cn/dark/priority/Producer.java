package cn.dark.priority;

import cn.dark.utils.ConnectionUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
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

    private static final String EXCHANGE_NAME = "priority exchange";
    private static final String QUEUE_NAME = "priority queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = ConnectionUtils.getConnection("192.168.0.106", 5672, "lwj", "lwj");
             Channel channel = connection.createChannel()) {
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("x-max-priority", 10); // 设置优先级
            channel.queueDeclare(QUEUE_NAME, false, false, false, arguments);
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

            for (int i = 0; i < 10; i++) {
                // 设置消息的优先级，优先级越高越先被消费，当且仅当消息积压时有效
                AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
                        .priority(i+1)
                        .build();
                channel.basicPublish(EXCHANGE_NAME, "", basicProperties, (i + "").getBytes());
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
