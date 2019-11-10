package cn.dark.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author dark
 * @date 2019-09-02
 */
public class TopicPersistentConsumer01 {

    public static void main(String[] args) {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.0.106:61616");
        Connection connection = null;
        try {
            connection = factory.createConnection();
            // 持久化连接需要设置唯一的clentId
            connection.setClientID("lwj");
            connection.start();

            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            // 创建接收队列
            Destination destination = session.createTopic("topic");
            // 创建持久化消息消费者，非持久化消费者只能消费在它启动后接收到的消息，不包含离线消息；
            // 而持久化消息消费者，需要先设置一个唯一的clentId，当怠机恢复后可接收到离线消息，
            MessageConsumer consumer = session.createDurableSubscriber((Topic) destination, "lwj");
            // 接收消息
            TextMessage message = (TextMessage) consumer.receive();
            System.out.println(message.getText());

            session.commit();
            session.close();
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
