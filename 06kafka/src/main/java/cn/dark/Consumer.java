package cn.dark;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author dark
 * @date 2019-09-15
 */
public class Consumer extends Thread {

    private final KafkaConsumer consumer;

    public Consumer(String topic) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.0.109:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"consumerDemo");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.IntegerDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        /*
            latest:新的消费者将会从其他消费者最后消费的offset 处开始消费 Topic 下的消息
            earliest:新的消费者会从该 Topic 最早的消息开始消费
            none:新的消费者加入以后，由于之前不存在offset，则会直接抛出异常。
         */
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        consumer = new KafkaConsumer(properties);
        consumer.subscribe(Collections.singletonList(topic));
    }

    @Override
    public void run() {
        while (true) {
            ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<Integer, String> record : records) {
                System.out.println("receive: " + record.value());
                // consumer.commitAsync(); // 未开启自动提交时需要手动提交
            }
        }
    }

    public static void main(String[] args) {
        new Consumer("test").start();
    }
}
