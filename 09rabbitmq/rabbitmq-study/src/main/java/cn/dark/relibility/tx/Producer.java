package cn.dark.relibility.tx;

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

    private static final String EXCHANGE_NAME = "tx exchange";
    private static final String QUEUE_NAME = "tx queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = null;
        Channel channel = null;
        try {
            connection =ConnectionUtils.getConnection("192.168.0.106", 5672, "lwj", "lwj");
            channel = connection.createChannel();
            // 开启事物模式
            channel.txSelect();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);

                channel.basicPublish(EXCHANGE_NAME, "", null, (i + "").getBytes());
                // 测试回滚，可以看到前6条被提交了，而后面的消息都回滚了
                if (i == 5) {
                    channel.txCommit();
                }
                if (i == 9) {
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            // 回滚
            channel.txRollback();
        } finally {
            channel.close();
            connection.close();
        }
    }

}
