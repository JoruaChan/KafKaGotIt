package cn.joruachan.kafka;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * 全局内容<br>
 *
 * @author JoruaChan
 */
public class Global {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.class);

    public static Admin ADMIN_CLIENT = null;
    public static Properties KAFKA_PROPERTIES = null;

    static {
        try {
            URL url = Global.class.getClassLoader().getResource("kafka.properties");
            KAFKA_PROPERTIES = PropertyReader.getProperties(url.getPath());
            ADMIN_CLIENT = KafkaAdminClient.create(KAFKA_PROPERTIES);
        } catch (IOException e) {
            LOGGER.error("获取KafKa配置失败!", e);
        }
    }
}
