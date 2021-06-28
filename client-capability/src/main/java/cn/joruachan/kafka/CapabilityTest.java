package cn.joruachan.kafka;

import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Uuid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * KafKa客户端能力测试类<br>
 *
 * @author JoruaChan
 */
public class CapabilityTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CapabilityTest.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final String topicName = "jc_test";

        if (!JCTopic.isTopicExist(topicName)) {
            // 创建一个topic
            CreateTopicsResult createTopicsResult = JCTopic.createNewTopic(topicName, 3);

            KafkaFuture<Uuid> kafkaFuture = createTopicsResult.topicId(topicName);
            kafkaFuture.whenComplete(((uuid, throwable) -> {
                if (throwable != null) {
                    LOGGER.info("创建Topic发生异常!", throwable);
                } else {
                    LOGGER.info("创建Topic结束!");
                }
            }));
        }

        // 创建一个生产者, 并发送消息
        Producer producer = JCProducer.createProducer();
        JCProducer.newProduceThread(producer, topicName);
    }
}
