package xyz.xiaosung.util;

import java.io.*;
import java.util.Properties;

public class ConfigManager {
    private static ConfigManager configManager;
    private Properties properties;

    private ConfigManager(String configFile) throws FileNotFoundException, UnsupportedEncodingException {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(configFile),"UTF-8");
        properties = new Properties();
        try {
            properties.load(isr);
            isr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConfigManager getInstance(String configFile) throws FileNotFoundException, UnsupportedEncodingException {
        if(configManager == null){
            configManager = new ConfigManager(configFile);
        }
        return configManager;
    }
    public String getString(String key) {
        return properties.getProperty(key);
    }
}

