package cn.dark.dlx;

import cn.dark.utils.ConnectionUtils;
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

    private static String test_exchange_name = "test dlx exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = ConnectionUtils.getConnection("192.168.0.106", 5672, "lwj", "lwj");
             Channel channel = connection.createChannel()) {

            String[] msg = {"ack", "reject", "nack", "ttl", "max1", "max2", "max3"};
            String routingKey = "reject";
            for (int i = 0; i < msg.length; i++) {
                if ("ttl".equals(msg[i])) {
                    routingKey = "ttl";
                    test_exchange_name = "test dlx exchange with ttl";
                }
                if (i > 3) {
                    routingKey = "maxLen";
                    test_exchange_name = "test dlx exchange with max length";
                }
                channel.basicPublish(test_exchange_name, routingKey, null, msg[i].getBytes());
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
