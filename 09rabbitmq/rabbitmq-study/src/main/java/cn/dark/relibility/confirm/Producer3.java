package cn.dark.relibility.confirm;

import cn.dark.utils.ConnectionUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author dark
 * @date 2019-11-08
 */
public class Producer3 {

    private static final String EXCHANGE_NAME = "confirm exchange";
    private static final String QUEUE_NAME = "confirm queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = null;
        Channel channel = null;
        try {
            connection =ConnectionUtils.getConnection("192.168.0.106", 5672, "lwj", "lwj");
            channel = connection.createChannel();
            // 开启确认模式
            channel.confirmSelect();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

            // 异步确认消息
            channel.addConfirmListener(new ConfirmListener() {
                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("确认消息tag: " + deliveryTag);
                }

                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("未确认消息tag: " + deliveryTag);
                }
            });

            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                channel.basicPublish(EXCHANGE_NAME, "", null, (i + "").getBytes());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.close();
            connection.close();
        }
    }

}
