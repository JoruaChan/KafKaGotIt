package cn.joruachan.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * 生产者对象<br>
 * 详细描述、功能使用方法
 *
 * @author JoruaChan
 */
public class JCProducer {
    private final static Logger LOGGER = LoggerFactory.getLogger(JCProducer.class);

    /**
     * 获取生产者
     *
     * @return
     */
    public static final Producer createProducer() {
        return new KafkaProducer(Global.KAFKA_PROPERTIES);
    }

    /**
     * 生产者发送消息
     *
     * @param producer
     * @param topicName
     */
    public static final void newProduceThread(Producer producer, String topicName) {
        new Thread(() -> {
            // 发一点停一会，再发一会
            int recycleCount = 5;
            while (recycleCount-- > 0) {
                int count = (int) (Math.random() * 1000);
                System.out.println(count);

                CountDownLatch countDownLatch = new CountDownLatch(count);
                for (int j = 0; j < count; j++) {
                    // 发送消息
                    producer.send(new ProducerRecord(topicName, (j + 1) + ""),
                            (metadata, exception) -> {
                                LOGGER.info("消息发送结束, offset: {}", metadata.offset());
                                countDownLatch.countDown();
                            });
                }

                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    LOGGER.error("线程中断！", e);
                }
            }
        }).start();
    }
}
