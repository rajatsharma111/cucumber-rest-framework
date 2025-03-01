package com.fancode.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static final Properties properties = new Properties();
    private static ConfigManager instance;

    private ConfigManager() {
        String env = System.getProperty("env");
        if (env == null || env.isEmpty()) {
            env = System.getenv("ENV");
        }
        if (env == null || env.isEmpty()) {
            env = "test";
        }
        loadEnvironmentConfig(env);
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadEnvironmentConfig(String environment) {
        try (FileInputStream fis = new FileInputStream("src/main/resources/config/" + environment + ".properties")) {
            Properties envProperties = new Properties();
            envProperties.load(fis);
            properties.putAll(envProperties);
            logger.info("Loaded {} environment configuration", environment);
        } catch (IOException e) {
            logger.error("Failed to load {} environment configuration", environment, e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}