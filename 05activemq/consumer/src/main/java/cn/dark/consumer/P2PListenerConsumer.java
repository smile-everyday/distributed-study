package cn.dark.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author dark
 * @date 2019-09-02
 */
public class P2PListenerConsumer {

    public static void main(String[] args) {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.0.106:61616");
        Connection connection = null;
        try {
            connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("queue");

            MessageConsumer consumer = session.createConsumer(destination);
            // 使用监听器监听队列
            MessageListener listener = new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        System.out.println(((TextMessage) message).getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            };
            while (true) {
                consumer.setMessageListener(listener);
                session.commit();
            }

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

}
