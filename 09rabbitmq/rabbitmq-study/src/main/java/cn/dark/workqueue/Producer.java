package cn.dark.workqueue;

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

    private static final String QUEUE_NAME = "work queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = null;
        try (Connection connection = ConnectionUtils.getConnection("192.168.0.106", 5672, "lwj", "lwj")) {
            channel =connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            for (int i = 0; i < 10; i++) {
                channel.basicPublish("", QUEUE_NAME, null, ("hello, " + i).getBytes());
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
