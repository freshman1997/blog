package com.yuan.cn.blog.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 2019-7-13 20:12
 * 属性读取工具类，可以加载 configuration.properties 文件中的所有 properties 文件的配置
 */
public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    /**
     * 配置属性文件路径的属性文件
     */
    private static final String summaryPropertiesFileName = "configuration.properties";
    /**
     * 保存属性文件路径的集合
     */
    private static List<String> propertiesFileList = new ArrayList<>();
    /**
     * classLoader
     */
    private static ClassLoader propertiesLoader = PropertiesUtil.class.getClassLoader();
    /**
     * 保存所有键值对的 map 集合
     */
    private static final Map<String, String> propertiesMap = new ConcurrentHashMap<>();

    static {

        InputStream is = propertiesLoader.getResourceAsStream(summaryPropertiesFileName);
        Properties props = new Properties();

        try {
            props.load(is);
            Enumeration<?> enumeration = props.propertyNames();
            int c = 0;
            while (enumeration.hasMoreElements()) {
                c++;
                propertiesFileList.add((String) enumeration.nextElement());
            }
            Properties[] properties = new Properties[c];
            // todo 加载所有的配置文件
            for (int i = 0; i < propertiesFileList.size(); i++) {
                properties[i] = new Properties();
                logger.info("time: {}, loading properties file for : {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), propertiesFileList.get(i));

                try {
                    properties[i].load(propertiesLoader.getResourceAsStream(props.getProperty(propertiesFileList.get(i))));
                    propertiesMap.put(propertiesFileList.get(i), props.getProperty(propertiesFileList.get(i)));
                } catch (Exception e) {
                    logger.error("配置文件 " + props.getProperty(propertiesFileList.get(i)) + " 读取异常 cause: properties file does not exits.");
                }
            }
            for (Properties property : properties) {

                Set<String> strings = property.stringPropertyNames();
                for (String string : strings) {
                    propertiesMap.put(string, property.getProperty(string));
                }
            }
        } catch (IOException e) {
            logger.error("配置文件读取异常", e);
        }
    }

    public static String getProperty(String key) {
        if (propertiesMap.containsKey(key.trim()))
            return propertiesMap.get(key.trim()).trim();
        throw new IllegalArgumentException("要寻找的属性不存在，请检查属性文件是否正确配置属性键值对！");
    }

    public static String getProperty(String key, String defaultValue) {
        if (propertiesMap.containsKey(key.trim())) {
            String result = propertiesMap.get(key.trim());

            if (StringUtils.isBlank(result)) {
                result = defaultValue;
            }
            return result.trim();
        }
        throw new IllegalArgumentException("要寻找的属性不存在，请检查属性文件是否正确配置属性键值对！");
    }

    public static void main(String[] args) throws ParseException, JsonProcessingException, SQLException {
    }
}
