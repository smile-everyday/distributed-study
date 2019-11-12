package cn.dark.relibility.routingEnsure;

import cn.dark.utils.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 备份交换器
 *
 * @author dark
 * @date 2019-11-08
 */
public class Producer2 {

    private static final String EXCHANGE_NAME = "test alternate exchange";
    private static final String QUEUE_NAME = "test alternate queue";

    public static final String ALTERNATE_EXCHANGE = "alternate exchange";
    public static final String ALTERNATE_QUEUE = "alternate queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = null;
        Channel channel = null;
        try {
            connection =ConnectionUtils.getConnection("192.168.0.106", 5672, "lwj", "lwj");
            channel = connection.createChannel();

            // 指定备份交换机
            // 如果备份交换机不存在，消息丢失，没有异常
            // 如果备份交换机未绑定队列，消息丢失，没有异常
            // 如果备份交换机没有匹配到任何队列，消息丢失，没有异常
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("alternate-exchange", ALTERNATE_EXCHANGE);
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, false, false, arguments);
            // 声明备份交换机
            channel.exchangeDeclare(ALTERNATE_EXCHANGE, BuiltinExchangeType.FANOUT, false, false, null);

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.queueDeclare(ALTERNATE_QUEUE, false, false, false, null);

            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "test alternate");
            channel.queueBind(ALTERNATE_QUEUE, ALTERNATE_EXCHANGE, "");

            for (int i = 0; i < 10; i++) {
                // 将mandatory设置为true，当未能成功路由到队列时，会触发监听器
                channel.basicPublish(EXCHANGE_NAME, "alternate", null, (i + "").getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.close();
            connection.close();
        }
    }

}
