package com.getgo.pages;

import com.getgo.drivermanager.DriverManager;
import com.getgo.utils.ElementUtil;
import com.getgo.utils.GestureUtil;
import com.getgo.utils.WaitUtil;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

/**
 * BasePage class that all page objects will extend
 * Contains common methods used across all pages
 */
public class BasePage {
    
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    protected AppiumDriver driver;
    
    public BasePage() {
        this.driver = DriverManager.getDriver();
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }
    
    /**
     * Click on element
     */
    public void click(By locator) {
        ElementUtil.click(locator);
    }
    
    /**
     * Click on WebElement
     */
    public void click(WebElement element) {
        ElementUtil.click(element);
    }
    
    /**
     * Send keys to element
     */
    public void sendKeys(By locator, String text) {
        ElementUtil.sendKeys(locator, text);
    }

    
    /**
     * Check if element is present
     */
    public boolean isElementPresent(By locator) {
        return ElementUtil.isElementPresent(locator);
    }
    
    /**
     * Check if element is not present
     */
    public boolean isElementNotPresent(By locator) {
        return ElementUtil.isElementNotPresent(locator);
    }

    
    /**
     * Wait for element to be visible
     */
    public WebElement waitForElementToBeVisible(By locator, int timeout) {
        return ElementUtil.waitForElementToBeVisible(locator, timeout);
    }
    
    /**
     * Wait for either of two elements to be present
     */
    public void waitForEitherElement(By locator1, By locator2, int timeout) {
        WaitUtil.waitForEitherElement(locator1, locator2, timeout);
    }
    
    /**
     * Scroll to element
     */
    public boolean scrollToElement(WebElement element, int maxScrolls) {
        return GestureUtil.scrollToElement(element, maxScrolls);
    }
    
    /**
     * Scroll to element by text
     */
    public boolean scrollToElementByText(String text, int maxScrolls) {
        return GestureUtil.scrollToElementByText(text, maxScrolls);
    }
    
    /**
     * Swipe up
     */
    public void swipeUp() {
        GestureUtil.swipeUp();
    }
    
    /**
     * Swipe left
     */
    public void swipeLeft() {
        GestureUtil.swipeLeft();
    }

    
    /**
     * Swipe left on a specific element
     * @param element Element to swipe on
     */
    public void swipeLeftOnElement(WebElement element) {
        GestureUtil.swipeLeftOnElement(element);
    }
    
    /**
     * Wait for specific time
     */
    public void waitFor(int seconds) {
        WaitUtil.waitFor(seconds);
    }
    
    /**
     * Navigate back
     */
    public void navigateBack() {
        driver.navigate().back();
        logger.info("Navigated back");
    }
}
