package com.getgo.drivermanager;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

/**
 * DriverManager class to manage Appium driver lifecycle
 * Implements Singleton pattern to ensure single driver instance
 */
public class DriverManager {
    
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    private static Properties capabilities;
    
    /**
     * Initialize the Appium driver with desired capabilities
     * @param caps DesiredCapabilities object
     */
    public static void initializeDriver(DesiredCapabilities caps) {
        try {
            String appiumServerURL = capabilities != null ? 
                capabilities.getProperty("appium.server.url", "http://127.0.0.1:4723") :
                "http://127.0.0.1:4723";
            
            logger.info("Initializing Appium Driver with URL: " + appiumServerURL);
            logger.info("Capabilities: " + caps.toString());
            
            AndroidDriver androidDriver = new AndroidDriver(new URL(appiumServerURL), caps);
            
            // Set implicit wait
            int implicitWait = capabilities != null ? 
                Integer.parseInt(capabilities.getProperty("implicit.wait", "10")) : 10;
            androidDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
            
            driver.set(androidDriver);
            logger.info("Appium Driver initialized successfully");
            
        } catch (MalformedURLException e) {
            logger.error("Invalid Appium Server URL", e);
            throw new RuntimeException("Failed to initialize driver: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error initializing driver", e);
            throw new RuntimeException("Failed to initialize driver: " + e.getMessage());
        }
    }
    
    /**
     * Get the current Appium driver instance
     * @return AppiumDriver instance
     */
    public static AppiumDriver getDriver() {
        if (driver.get() == null) {
            logger.error("Driver is not initialized. Please call initializeDriver first.");
            throw new IllegalStateException("Driver is not initialized");
        }
        return driver.get();
    }
    
    /**
     * Quit the driver and cleanup
     */
    public static void quitDriver() {
        if (driver.get() != null) {
            try {
                logger.info("Quitting Appium Driver");
                driver.get().quit();
                driver.remove();
                logger.info("Appium Driver quit successfully");
            } catch (Exception e) {
                logger.error("Error while quitting driver", e);
            }
        }
    }
    
    /**
     * Set capabilities properties
     * @param props Properties object containing capabilities
     */
    public static void setCapabilities(Properties props) {
        capabilities = props;
    }
    
    /**
     * Get capabilities properties
     * @return Properties object
     */
    public static Properties getCapabilities() {
        return capabilities;
    }
    
    /**
     * Check if driver is initialized
     * @return boolean true if driver is initialized
     */
    public static boolean isDriverInitialized() {
        return driver.get() != null;
    }
    
    /**
     * Reset the app
     */
    public static void resetApp() {
        if (driver.get() != null) {
            logger.info("Resetting the app");
            ((AndroidDriver) driver.get()).terminateApp(
                capabilities.getProperty("app.package")
            );
            ((AndroidDriver) driver.get()).activateApp(
                capabilities.getProperty("app.package")
            );
        }
    }
    
    /**
     * Close the app
     */
    public static void closeApp() {
        if (driver.get() != null) {
            logger.info("Closing the app");
            ((AndroidDriver) driver.get()).terminateApp(
                capabilities.getProperty("app.package")
            );
        }
    }
}
