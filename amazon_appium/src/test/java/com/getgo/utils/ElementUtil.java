package com.getgo.utils;

import com.getgo.drivermanager.DriverManager;
import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.List;

/**
 * ElementUtil provides utility methods for element interactions
 * Includes click, verify presence, send keys, and other common operations
 */
public class ElementUtil {
    
    private static final Logger logger = LogManager.getLogger(ElementUtil.class);
    private static final int DEFAULT_TIMEOUT = 20;
    
    /**
     * Click on element with wait
     * @param locator By locator for element
     */
    public static void click(By locator) {
        try {
            WebElement element = waitForElementToBeClickable(locator, DEFAULT_TIMEOUT);
            element.click();
            logger.info("Clicked on element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to click on element: " + locator, e);
            throw new RuntimeException("Failed to click element: " + e.getMessage());
        }
    }
    
    /**
     * Click on WebElement
     * @param element WebElement to click
     */
    public static void click(WebElement element) {
        try {
            element.click();
            logger.info("Clicked on element");
        } catch (Exception e) {
            logger.error("Failed to click on element", e);
            throw new RuntimeException("Failed to click element: " + e.getMessage());
        }
    }
    
    /**
     * Send keys to element
     * @param locator By locator for element
     * @param text Text to send
     */
    public static void sendKeys(By locator, String text) {
        try {
            WebElement element = waitForElementToBeVisible(locator, DEFAULT_TIMEOUT);
            element.clear();
            element.sendKeys(text);
            logger.info("Sent keys to element: " + locator);
        } catch (Exception e) {
            logger.error("Failed to send keys to element: " + locator, e);
            throw new RuntimeException("Failed to send keys: " + e.getMessage());
        }
    }
    
    /**
     * Verify if element is present
     * @param locator By locator for element
     * @return true if element is present, false otherwise
     */
    public static boolean isElementPresent(By locator) {
        try {
            AppiumDriver driver = DriverManager.getDriver();
            driver.findElement(locator);
            logger.info("Element is present: " + locator);
            return true;
        } catch (NoSuchElementException e) {
            logger.info("Element is not present: " + locator);
            return false;
        } catch (Exception e) {
            logger.error("Error checking element presence: " + locator, e);
            return false;
        }
    }
    
    /**
     * Verify if element is not present
     * @param locator By locator for element
     * @return true if element is not present, false otherwise
     */
    public static boolean isElementNotPresent(By locator) {
        boolean notPresent = !isElementPresent(locator);
        logger.info("Element is not present: " + locator + " - " + notPresent);
        return notPresent;
    }
    
    /**
     * Verify if element is displayed
     * @param locator By locator for element
     * @return true if element is displayed, false otherwise
     */
    public static boolean isElementDisplayed(By locator) {
        try {
            WebElement element = DriverManager.getDriver().findElement(locator);
            boolean displayed = element.isDisplayed();
            logger.info("Element is displayed: " + locator + " - " + displayed);
            return displayed;
        } catch (Exception e) {
            logger.info("Element is not displayed: " + locator);
            return false;
        }
    }
    
    /**
     * Wait for element to be visible
     * @param locator By locator for element
     * @param timeout Timeout in seconds
     * @return WebElement
     */
    public static WebElement waitForElementToBeVisible(By locator, int timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.info("Element is visible: " + locator);
            return element;
        } catch (Exception e) {
            logger.error("Element not visible within timeout: " + locator, e);
            throw new RuntimeException("Element not visible: " + e.getMessage());
        }
    }
    
    /**
     * Wait for element to be clickable
     * @param locator By locator for element
     * @param timeout Timeout in seconds
     * @return WebElement
     */
    public static WebElement waitForElementToBeClickable(By locator, int timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            logger.info("Element is clickable: " + locator);
            return element;
        } catch (Exception e) {
            logger.error("Element not clickable within timeout: " + locator, e);
            throw new RuntimeException("Element not clickable: " + e.getMessage());
        }
    }
    
    /**
     * Wait for element to be invisible
     * @param locator By locator for element
     * @param timeout Timeout in seconds
     * @return true if element becomes invisible
     */
    public static boolean waitForElementToBeInvisible(By locator, int timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeout));
            boolean invisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
            logger.info("Element is invisible: " + locator);
            return invisible;
        } catch (Exception e) {
            logger.error("Element still visible after timeout: " + locator, e);
            return false;
        }
    }

    /**
     * Get list of elements
     * @param locator By locator for elements
     * @return List of WebElements
     */
    public static List<WebElement> getElements(By locator) {
        try {
            List<WebElement> elements = DriverManager.getDriver().findElements(locator);
            logger.info("Found " + elements.size() + " elements: " + locator);
            return elements;
        } catch (Exception e) {
            logger.error("Failed to get elements: " + locator, e);
            throw new RuntimeException("Failed to get elements: " + e.getMessage());
        }
    }
    
    /**
     * Take screenshot
     * @param fileName Name for screenshot file
     * @return Path to screenshot file
     */
    public static String takeScreenshot(String fileName) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) DriverManager.getDriver();
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            String destPath = "screenshots/" + fileName + "_" + System.currentTimeMillis() + ".png";
            File destFile = new File(destPath);
            destFile.getParentFile().mkdirs();
            org.apache.commons.io.FileUtils.copyFile(srcFile, destFile);
            logger.info("Screenshot saved: " + destPath);
            return destPath;
        } catch (Exception e) {
            logger.error("Failed to take screenshot", e);
            return null;
        }
    }

}
