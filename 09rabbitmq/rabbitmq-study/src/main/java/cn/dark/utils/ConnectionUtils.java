package cn.dark.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author dark
 * @date 2019-11-08
 */
public class ConnectionUtils {

    private ConnectionUtils() {}

    public static Connection getConnection(String host, Integer port, String username, String password) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setVirtualHost("/");
        factory.setUsername(username);
        factory.setPassword(password);

        return factory.newConnection();
    }

}
