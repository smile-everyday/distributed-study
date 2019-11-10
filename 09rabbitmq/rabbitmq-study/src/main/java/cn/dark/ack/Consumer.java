package cn.dark.ack;

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
public class Consumer {

    private static final String QUEUE_NAME = "ack queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection("192.168.0.106", 5672, "lwj", "lwj");
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        DeliverCallback callback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), "utf8");
            System.out.println("receive msg: " + msg);

            if (1 == Integer.valueOf(msg) % 2) {
                // 手动应答
                // multiple: 是否批量应答，true表示会批量确认当前tag及小于当前tag且未被确认的消息
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } else if (0 == Integer.valueOf(msg) % 2) {
                // 单条拒收消息
                // requeue：拒收后是否重新入队，true会重新入队导致重复消费消息，false则是直接删除
                // 若只有一个队列且设置重新入队，就会导致死循环消费
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), true);
            } else {
                // 批量拒收消息
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), true, true);

            }
        };
        channel.basicConsume(QUEUE_NAME, false, callback, consumerTag -> {});
    }

}
