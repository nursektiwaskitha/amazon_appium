package com.getgo.utils;

import com.getgo.drivermanager.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WaitUtil provides explicit wait utility methods
 */
public class WaitUtil {
    
    private static final Logger logger = LogManager.getLogger(WaitUtil.class);

    /**
     * Wait for element to be present
     * @param locator By locator
     * @param timeout Timeout in seconds
     * @return WebElement
     */
    public static WebElement waitForElementPresence(By locator, int timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (Exception e) {
            logger.error("Element not present: " + locator, e);
            throw e;
        }
    }
    
    /**
     * Wait for specific time
     * @param seconds Seconds to wait
     */
    public static void waitFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            logger.error("Wait interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Wait for either of two elements to be present
     * @param locator1 First By locator
     * @param locator2 Second By locator
     * @param timeout Timeout in seconds
     */
    public static void waitForEitherElement(By locator1, By locator2, int timeout) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
        wait.until(driver -> 
            ElementUtil.isElementPresent(locator1) || ElementUtil.isElementPresent(locator2)
        );
    }
}
