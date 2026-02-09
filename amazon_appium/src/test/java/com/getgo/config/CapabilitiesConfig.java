package com.getgo.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * CapabilitiesConfig class to load and configure desired capabilities
 * for Appium driver from properties file
 */
public class CapabilitiesConfig {
    
    private static final Logger logger = LogManager.getLogger(CapabilitiesConfig.class);
    private static Properties properties;
    private static final String CONFIG_FILE = "src/test/resources/config/capabilities.properties";
    
    /**
     * Load capabilities from properties file
     * @return Properties object containing all capabilities
     */
    public static Properties loadProperties() {
        if (properties == null) {
            properties = new Properties();
            try (InputStream input = new FileInputStream(CONFIG_FILE)) {
                properties.load(input);
                logger.info("Capabilities loaded from: " + CONFIG_FILE);
            } catch (IOException e) {
                logger.error("Failed to load capabilities file: " + CONFIG_FILE, e);
                throw new RuntimeException("Could not load capabilities file", e);
            }
        }
        return properties;
    }
    
    /**
     * Get Android desired capabilities
     * @return DesiredCapabilities object configured for Android
     */
    public static DesiredCapabilities getAndroidCapabilities() {
        Properties props = loadProperties();
        DesiredCapabilities caps = new DesiredCapabilities();
        
        // Platform capabilities
        caps.setCapability("platformName", props.getProperty("platform.name", "Android"));
        caps.setCapability("platformVersion", props.getProperty("platform.version", "14.0"));
        caps.setCapability("deviceName", props.getProperty("device.name", "Android Emulator"));
        caps.setCapability("automationName", props.getProperty("automation.name", "UiAutomator2"));
        
        // App capabilities
        caps.setCapability("appPackage", props.getProperty("app.package"));
        caps.setCapability("appActivity", props.getProperty("app.activity"));
        
        // Optional: App path if provided
        if (props.getProperty("app.path") != null && !props.getProperty("app.path").isEmpty()) {
            caps.setCapability("app", props.getProperty("app.path"));
        }
        
        // Additional capabilities
        caps.setCapability("autoGrantPermissions", 
            Boolean.parseBoolean(props.getProperty("auto.grant.permissions", "true")));
        caps.setCapability("noReset", 
            Boolean.parseBoolean(props.getProperty("no.reset", "true")));
        caps.setCapability("fullReset", 
            Boolean.parseBoolean(props.getProperty("full.reset", "false")));
        
        // Performance capabilities
        caps.setCapability("newCommandTimeout", 300);
        caps.setCapability("androidInstallTimeout", 90000);
        
        logger.info("Android Capabilities configured: " + caps.toString());
        return caps;
    }
    
    /**
     * Get specific property value
     * @param key Property key
     * @return Property value
     */
    public static String getProperty(String key) {
        if (properties == null) {
            loadProperties();
        }
        return properties.getProperty(key);
    }
    
    /**
     * Get property with default value
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default value
     */
    public static String getProperty(String key, String defaultValue) {
        if (properties == null) {
            loadProperties();
        }
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get timeout values
     * @return Timeout properties
     */
    public static TimeoutConfig getTimeouts() {
        if (properties == null) {
            loadProperties();
        }
        return new TimeoutConfig(
            Integer.parseInt(properties.getProperty("implicit.wait", "10")),
            Integer.parseInt(properties.getProperty("explicit.wait", "20"))
        );
    }
    
    /**
     * Inner class for timeout configuration
     */
    public static class TimeoutConfig {
        private final int implicitWait;
        private final int explicitWait;
        
        public TimeoutConfig(int implicitWait, int explicitWait) {
            this.implicitWait = implicitWait;
            this.explicitWait = explicitWait;
        }
        
        public int getImplicitWait() {
            return implicitWait;
        }
        
        public int getExplicitWait() {
            return explicitWait;
        }
    }
}
