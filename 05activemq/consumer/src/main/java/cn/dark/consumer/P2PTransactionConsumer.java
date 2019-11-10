package cn.dark.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author dark
 * @date 2019-09-02
 */
public class P2PTransactionConsumer {

    public static void main(String[] args) {
        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.0.106:61616");
        Connection connection = null;
        try {
            connection = factory.createConnection();
            connection.start();

            // 在非事务型会话中，AUTO_ACKNOWLEDGE会在receive后自动签收消息，而CLIENT_ACKNOWLEDGE需要调用acknowledge方法才会签收
            // 在事务型会话中，默认使用的是SESSION_TRANSACTED，AUTO_ACKNOWLEDGE和CLIENT_ACKNOWLEDGE设置了无效
            // 而未提交或者rollback都会导致消息重发
            // DUPS_OK_ACKNOWLEDGE是延迟自动批量签收，若mq怠机，那些未签收的消息就会重发
            Session session = connection.createSession(false, Session.DUPS_OK_ACKNOWLEDGE);
            // 创建接收队列
            Destination destination = session.createQueue("queue");
            // 创建消息消费者
            MessageConsumer consumer = session.createConsumer(destination);
            // 接收消息
            TextMessage message = (TextMessage) consumer.receive();
            // message.acknowledge();
            System.out.println(message.getText());

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
