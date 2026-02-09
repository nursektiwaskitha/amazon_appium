package com.getgo.utils;

import com.getgo.drivermanager.DriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

/**
 * GestureUtil provides methods for performing gestures on mobile devices
 * Includes swipe, scroll, zoom, drag, and other touch actions
 */
public class GestureUtil {
    
    private static final Logger logger = LogManager.getLogger(GestureUtil.class);
    
    /**
     * Swipe from one point to another
     * @param startX Start X coordinate
     * @param startY Start Y coordinate
     * @param endX End X coordinate
     * @param endY End Y coordinate
     * @param duration Duration of swipe in milliseconds
     */
    public static void swipe(int startX, int startY, int endX, int endY, int duration) {
        try {
            AppiumDriver driver = DriverManager.getDriver();
            
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1);
            
            swipe.addAction(finger.createPointerMove(Duration.ZERO, 
                PointerInput.Origin.viewport(), startX, startY));
            swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(duration),
                PointerInput.Origin.viewport(), endX, endY));
            swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            
            driver.perform(Collections.singletonList(swipe));
            logger.info(String.format("Swiped from (%d,%d) to (%d,%d)", startX, startY, endX, endY));
            
        } catch (Exception e) {
            logger.error("Error performing swipe", e);
            throw new RuntimeException("Failed to perform swipe: " + e.getMessage());
        }
    }
    
    /**
     * Swipe up on the screen
     */
    public static void swipeUp() {
        AppiumDriver driver = DriverManager.getDriver();
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * 0.2);
        swipe(startX, startY, startX, endY, 800);
    }
    
    /**
     * Swipe left on the screen
     */
    public static void swipeLeft() {
        AppiumDriver driver = DriverManager.getDriver();
        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.width * 0.8);
        int endX = (int) (size.width * 0.2);
        int startY = size.height / 2;
        swipe(startX, startY, endX, startY, 800);
    }
    
    /**
     * Swipe left on a specific element
     * @param element Element to swipe on
     */
    public static void swipeLeftOnElement(WebElement element) {
        try {
            Point location = element.getLocation();
            Dimension size = element.getSize();
            
            int startX = location.x + (int) (size.width * 0.8);
            int endX = location.x + (int) (size.width * 0.2);
            int centerY = location.y + (size.height / 2);
            
            swipe(startX, centerY, endX, centerY, 800);
            logger.info("Swiped left on element");
        } catch (Exception e) {
            logger.error("Error swiping left on element", e);
            throw new RuntimeException("Failed to swipe left on element: " + e.getMessage());
        }
    }
    
    /**
     * Scroll until element is visible
     * @param element Target WebElement to scroll to
     * @param maxScrolls Maximum number of scroll attempts
     * @return true if element is found, false otherwise
     */
    public static boolean scrollToElement(WebElement element, int maxScrolls) {
        try {
            for (int i = 0; i < maxScrolls; i++) {
                if (element.isDisplayed()) {
                    logger.info("Element found after " + i + " scrolls");
                    return true;
                }
                swipeUp();
                Thread.sleep(500);
            }
            logger.warn("Element not found after " + maxScrolls + " scrolls");
            return false;
        } catch (Exception e) {
            logger.error("Error scrolling to element", e);
            return false;
        }
    }
    
    /**
     * Scroll until element is visible using text
     * @param text Text to search for
     * @param maxScrolls Maximum number of scroll attempts
     * @return true if element is found, false otherwise
     */
    public static boolean scrollToElementByText(String text, int maxScrolls) {
        try {
            AppiumDriver driver = DriverManager.getDriver();
            for (int i = 0; i < maxScrolls; i++) {
                try {
                    WebElement element = driver.findElement(
                        By.xpath("//*[contains(@text, '" + text + "')]"));
                    if (element.isDisplayed()) {
                        logger.info("Element with text '" + text + "' found after " + i + " scrolls");
                        return true;
                    }
                } catch (NoSuchElementException e) {
                    // Element not found yet, continue scrolling
                }
                swipeUp();
                Thread.sleep(500);
            }
            logger.warn("Element with text '" + text + "' not found after " + maxScrolls + " scrolls");
            return false;
        } catch (Exception e) {
            logger.error("Error scrolling to element by text", e);
            return false;
        }
    }
    
    /**
     * Zoom in on element
     * @param element Element to zoom in on
     */
    public static void zoomIn(WebElement element) {
        try {
            AppiumDriver driver = DriverManager.getDriver();
            Point location = element.getLocation();
            Dimension size = element.getSize();
            
            int centerX = location.x + (size.width / 2);
            int centerY = location.y + (size.height / 2);
            
            // Create two fingers for pinch gesture
            PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
            PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");
            
            // Start points (closer together)
            int startOffset = 50;
            int endOffset = 200;
            
            Sequence sequence1 = new Sequence(finger1, 0);
            sequence1.addAction(finger1.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), centerX - startOffset, centerY));
            sequence1.addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            sequence1.addAction(finger1.createPointerMove(Duration.ofMillis(500),
                PointerInput.Origin.viewport(), centerX - endOffset, centerY));
            sequence1.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            
            Sequence sequence2 = new Sequence(finger2, 0);
            sequence2.addAction(finger2.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), centerX + startOffset, centerY));
            sequence2.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            sequence2.addAction(finger2.createPointerMove(Duration.ofMillis(500),
                PointerInput.Origin.viewport(), centerX + endOffset, centerY));
            sequence2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            
            driver.perform(Arrays.asList(sequence1, sequence2));
            logger.info("Zoomed in on element");
            
        } catch (Exception e) {
            logger.error("Error performing zoom in", e);
            throw new RuntimeException("Failed to zoom in: " + e.getMessage());
        }
    }
    
    /**
     * Zoom out on element
     * @param element Element to zoom out on
     */
    public static void zoomOut(WebElement element) {
        try {
            AppiumDriver driver = DriverManager.getDriver();
            Point location = element.getLocation();
            Dimension size = element.getSize();
            
            int centerX = location.x + (size.width / 2);
            int centerY = location.y + (size.height / 2);
            
            // Create two fingers for pinch gesture
            PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
            PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");
            
            // Start points (farther apart)
            int startOffset = 200;
            int endOffset = 50;
            
            Sequence sequence1 = new Sequence(finger1, 0);
            sequence1.addAction(finger1.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), centerX - startOffset, centerY));
            sequence1.addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            sequence1.addAction(finger1.createPointerMove(Duration.ofMillis(500),
                PointerInput.Origin.viewport(), centerX - endOffset, centerY));
            sequence1.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            
            Sequence sequence2 = new Sequence(finger2, 0);
            sequence2.addAction(finger2.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), centerX + startOffset, centerY));
            sequence2.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            sequence2.addAction(finger2.createPointerMove(Duration.ofMillis(500),
                PointerInput.Origin.viewport(), centerX + endOffset, centerY));
            sequence2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            
            driver.perform(Arrays.asList(sequence1, sequence2));
            logger.info("Zoomed out on element");
            
        } catch (Exception e) {
            logger.error("Error performing zoom out", e);
            throw new RuntimeException("Failed to zoom out: " + e.getMessage());
        }
    }
}
