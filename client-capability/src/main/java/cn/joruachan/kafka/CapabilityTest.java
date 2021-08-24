package cn.joruachan.kafka;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Uuid;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
            Uuid uuid = kafkaFuture.get();
            LOGGER.info("创建Topic结束, uuid: {}!", uuid.toString());
        }

        // 创建一个生产者, 并发送消息
        Producer producer = JCProducer.createProducer();
        JCProducer.newProduceThread(producer, topicName);

//        // 等个两秒，再消费
//        Thread.sleep(2000);
//
//        final String consumerGroupName = "jcTest";
//
//        for (int i = 0; i < 5; i++) {
//            KafkaConsumer<String, String> consumer = JCConsumer.createOneConsumer(consumerGroupName);
//            Runnable runnable = new JCConsumer(consumer, topicName);
//            new Thread(runnable).start();
//        }
    }
}
