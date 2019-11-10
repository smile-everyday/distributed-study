package cn.dark.producer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author dark
 * @date 2019-09-02
 */
public class TopicProducer {

    public static void main(String[] args) {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.0.106:61616");
        Connection connection = null;
        try {
            // 创建并开启连接
            connection = factory.createConnection();
            connection.start();

            // 创建会话
            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            // 创建发送队列
            Destination destination = session.createTopic("topic");
            // 创建消息发送者
            MessageProducer producer = session.createProducer(destination);
            // 将消息持久化保存，防止丢失，但影响性能
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            // 创建消息并设置消息内容
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText("Hello");
            // 发送消息
            producer.send(textMessage);

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
