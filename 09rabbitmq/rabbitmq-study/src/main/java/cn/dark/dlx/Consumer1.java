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
public class Consumer1 {

    private static final String TEST_QUEUE_NAME = "test dlx queue";
    private static final String DLX_QUEUE_NAME = "dlx queue";

    private static final String TEST_EXCHANGE_NAME = "test dlx exchange";
    private static final String DLX_EXCHANGE_NAME = "dlx exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 这里最好不要使用try-with语法，在异步接收消息情况下会导致连接关闭
        Connection connection = ConnectionUtils.getConnection("192.168.0.106", 5672, "lwj", "lwj");
        Channel channel = connection.createChannel();
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DLX_EXCHANGE_NAME); // 将队列和死信队列进行绑定
        channel.exchangeDeclare(TEST_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(TEST_QUEUE_NAME, false, false, false, arguments);
        channel.queueBind(TEST_QUEUE_NAME, TEST_EXCHANGE_NAME, "reject");

        // 创建死信队列及交换机
        channel.exchangeDeclare(DLX_EXCHANGE_NAME, BuiltinExchangeType.DIRECT, false, false, false, null);
        channel.queueDeclare(DLX_QUEUE_NAME, false, false, false, null);
        // 需要注意如果与该死信队列绑定的队列指定了routingKey，死信队列也需要指定所有需要接收的routingKey，否则会收不到消息
        channel.queueBind(DLX_QUEUE_NAME, DLX_EXCHANGE_NAME, "reject");

        System.out.println("waitting msg......");
        DeliverCallback callback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), "utf8");
            System.out.println("receive msg: " + msg);

            // (reject || nack) && requeue == false会进入死信队列
            if ("ack".equals(msg)) {
                // 手动应答
                // multiple: 是否批量应答，true表示会批量确认当前tag及小于当前tag且未被确认的消息
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } else if ("reject".equals(msg)) {
                // 单条拒收消息
                // requeue：拒收后是否重新入队，true会重新入队导致重复消费消息，false则是直接删除
                channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
            } else if ("nack".equals(msg)) {
                // 批量拒收消息
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), true, false);

            }
        };
        channel.basicConsume(TEST_QUEUE_NAME, false, callback, consumerTag -> {});

        // 消费死信队列
        DeliverCallback dlxCallback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), "utf8");
            System.out.println("dlx msg: " + msg);

        };
        channel.basicConsume(DLX_QUEUE_NAME, true, dlxCallback, consumerTag -> {});
    }

}
