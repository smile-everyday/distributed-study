package cn.dark.relibility.routingEnsure;

import cn.dark.utils.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

/**
 * mandatory
 *
 * @author dark
 * @date 2019-11-08
 */
public class Producer1 {

    private static final String EXCHANGE_NAME = "mandatory exchange";
    private static final String QUEUE_NAME = "mandatory queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = null;
        Channel channel = null;
        try {
            connection =ConnectionUtils.getConnection("192.168.0.106", 5672, "lwj", "lwj");
            channel = connection.createChannel();
            channel.addReturnListener(new ReturnCallback() {
                @Override
                public void handle(Return returnMessage) {
                    try {
                        System.out.println("消息未能成功路由到队列：" + new String(returnMessage.getBody(), "utf8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

            for (int i = 0; i < 10; i++) {
                // 将mandatory设置为true，当未能成功路由到队列时，会触发监听器
                channel.basicPublish(EXCHANGE_NAME, "test mandatory", true, null, (i + "").getBytes());

                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.close();
            connection.close();
        }
    }

}
