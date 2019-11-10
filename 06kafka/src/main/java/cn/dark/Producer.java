package cn.dark;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @author dark
 * @date 2019-09-15
 */
public class Producer extends Thread {

    private final KafkaProducer<Integer, String> producer;
    private final String topic;

    public Producer(String topic) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.0.109:9092");
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "producerDemo");
        /*
            0：表示 producer 不需要等待 broker 的消息确认。这个选项时延最小但同时风险最大
                （因为当 server 宕机时，数据将会丢失）。
            1：表示 producer 只需要获得 kafka 集群中的 leader 节点确认即可，这个选择时延
                较小同时确保了 leader 节点确认接收成功。
            -1：需要 ISR 中所有的 Replica 给予接收确认，速度最慢，安全性最高，但是由于
                ISR 可能会缩小到仅包含一个 Replica，所以设置参数为 all 并不能一定避免数据丢失
         */
        properties.put(ProducerConfig.ACKS_CONFIG, "0");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.IntegerSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<Integer, String>(properties);
        this.topic = topic;
    }

    @Override
    public void run() {
        int n = 0;
        while (n < 100) {
            String message = "message_" + n;
            System.out.println("send: " + message);
            producer.send(new ProducerRecord<Integer, String>(topic, message));
            n++;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        new Producer("test").start();
    }
}
