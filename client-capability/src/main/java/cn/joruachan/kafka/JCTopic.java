package cn.joruachan.kafka;

import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * kafka的topic<br>
 * 详细描述、功能使用方法
 *
 * @author JoruaChan
 */
public class JCTopic {

    /**
     * 创建一个Topic
     *
     * @param name           topic名称
     * @param partitionCount 分区个数
     * @return
     * @throws IOException
     */
    public static final CreateTopicsResult createNewTopic(String name, int partitionCount) {
        NewTopic topic = new NewTopic(name, partitionCount, (short) 3);

        Collection<NewTopic> topics = new ArrayList<>();
        topics.add(topic);

        return Global.ADMIN_CLIENT.createTopics(topics);
    }

    /**
     * 判断topic是否已经存在
     *
     * @param name
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static final boolean isTopicExist(String name) throws ExecutionException, InterruptedException {
        Collection<String> topicNames = new ArrayList<>();
        topicNames.add(name);

        ListTopicsResult listTopicsResult = Global.ADMIN_CLIENT.listTopics();
        Set<String> names = listTopicsResult.names().get();
        return names.contains(name);
    }
}
