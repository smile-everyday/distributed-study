package cn.dark.basic;

import cn.dark.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author dark
 * @date 2019-11-08
 */
public class Producer {

    private static final String QUEUE_NAME = "myQueue";

    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = ConnectionUtils.getConnection("192.168.0.106", 5672, "lwj", "lwj");
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // 发送到直连类型的amqp default exchange，只要有队列名字和routing key相同就能收到消息
            channel.basicPublish("", QUEUE_NAME, null, "Hello, world!".getBytes());
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
