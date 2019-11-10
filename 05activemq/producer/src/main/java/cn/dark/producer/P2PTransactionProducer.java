package cn.dark.producer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author dark
 * @date 2019-09-02
 */
public class P2PTransactionProducer {

    public static void main(String[] args) {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.0.106:61616");
        Connection connection = null;
        try {
            // 创建并开启连接
            connection = factory.createConnection();
            connection.start();

            // 事务型会话设置为true，需要commit才能发送消息，而设置为false会自动提交
            // acknowledgeMode和生产者没有太大关系，但SESSION_TRANSACTED模式和非事务型会话是冲突的
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 创建发送队列
            Destination destination = session.createQueue("queue");
            // 创建消息发送者
            MessageProducer producer = session.createProducer(destination);
            // 创建消息并设置消息内容
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText("Hello");
            // 发送消息
            producer.send(textMessage);

            // session.rollback();
            // session.commit();
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
