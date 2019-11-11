package cn.dark.direct;

import cn.dark.utils.ConnectionUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author dark
 * @date 2019-11-08
 */
public class Producer {

    private static final String QUEUE_NAME = "direct queue";
    private static final String EXCHANGE_NAME = "exchange queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = ConnectionUtils.getConnection("192.168.0.106", 5672, "lwj", "lwj");
             Channel channel = connection.createChannel()) {
            // 声明直连类型的交换机
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            // 参数含义：1. 队列名称 2. 是否持久化 3. 该队列是否仅对首次声明它的连接可见 4. 是否自动删除 5. 队列参数
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // 绑定交换机和队列，并指定routingKey
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "direct");

            // 发送消息，只有routingKey为direct的队列才能接收到这条消息
            channel.basicPublish(EXCHANGE_NAME, "direct", null, "Hello, world!".getBytes());
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
