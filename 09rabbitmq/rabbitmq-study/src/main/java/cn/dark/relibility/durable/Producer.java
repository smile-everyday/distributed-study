package cn.dark.relibility.durable;

import cn.dark.utils.ConnectionUtils;
import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.AMQBasicProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 保证消息正确存储的方式：
 *         持久化交换机
 *         持久化队列
 *         持久化消息
 *         集群镜像队列
 *
 * @author dark
 * @date 2019-11-08
 */
public class Producer {

    private static final String EXCHANGE_NAME = "durable exchange";
    private static final String QUEUE_NAME = "durable queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = null;
        Channel channel = null;
        try {
            connection =ConnectionUtils.getConnection("192.168.0.106", 5672, "lwj", "lwj");
            channel = connection.createChannel();

            // 声明持久化交换器
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true, false, null);
            // 声明持久化队列
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

            for (int i = 0; i < 10; i++) {
                // 消息持久化
                AMQP.BasicProperties build = new AMQP.BasicProperties().builder()
                        .deliveryMode(2)
                        .build();
                channel.basicPublish(EXCHANGE_NAME, "", build, (i + "").getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.close();
            connection.close();
        }
    }

}
