package cn.dark.fanout;

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

    private static final String EXCHANGE_NAME = "fanout exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        try (Connection connection = ConnectionUtils.getConnection("192.168.0.106", 5672, "lwj", "lwj");
             Channel channel =connection.createChannel()) {
            // 创建一个fanout类型的交换机
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            for (int i = 0; i < 10; i++) {
                // fanout类型交换机不需要指定routing key，所有绑定该交换机的队列都可以接收到消息
                channel.basicPublish(EXCHANGE_NAME, "", null, ("hello, " + i).getBytes());
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
