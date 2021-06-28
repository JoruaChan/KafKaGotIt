package cn.joruachan.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 读取文件<br>
 *
 * @author JoruaChan
 */
public class PropertyReader {

    private static final Map<String, Properties> PROPERTIES_MAP = new ConcurrentHashMap<>();

    /**
     * 根据文件名获取Properties
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static final Properties getProperties(String filePath) throws IOException {
        Properties properties = PROPERTIES_MAP.get(filePath);
        if (properties == null) {
            properties = new Properties();
            properties.load(new FileReader(filePath));

            PROPERTIES_MAP.put(filePath, properties);
        }
        return properties;
    }
}
