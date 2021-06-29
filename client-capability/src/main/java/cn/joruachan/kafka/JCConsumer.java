package cn.joruachan.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;

/**
 * kafka的消费者<br>
 *
 * @author JoruaChan
 */
public class JCConsumer implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(JCConsumer.class);

    public static Properties CONSUMER_PROP = null;

    static {
        URL url = Global.class.getClassLoader().getResource("consumer.properties");
        try {
            CONSUMER_PROP = PropertyReader.getProperties(url.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一个Consumer
     *
     * @return
     */
    public static final KafkaConsumer<String, String> createOneConsumer(String groupName) {
        Properties properties = new Properties();
        properties.putAll(CONSUMER_PROP);

        // consumer group
        properties.put("group.id", groupName);

        return new KafkaConsumer<>(properties);
    }

    private KafkaConsumer<String, String> consumer;
    private String topic;
    private boolean close = false;

    public JCConsumer(KafkaConsumer<String, String> consumer, String topic) {
        this.consumer = consumer;
        this.topic = topic;
    }

    @Override
    public void run() {
        // 先订阅Topic
        this.consumer.subscribe(Arrays.asList(this.topic));

        // 有消息就消费吧
        while (!close) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));
            for (TopicPartition topicPartition : records.partitions()) {
                LOG.info("topic：{} 当前的partition是：{}", topicPartition.topic(), topicPartition.partition());
            }

            Iterator<ConsumerRecord<String, String>> iterable = records.records(this.topic).iterator();
            while (iterable.hasNext()) {
                ConsumerRecord<String, String> record = iterable.next();
                LOG.info("收到消息, 其offset: {}, 内容: {}", record.offset(), record.value());
            }

            // 手动ack
            consumer.commitSync();
        }
    }

    public void shutdown() {
        close = true;
        consumer.wakeup();
    }
}
