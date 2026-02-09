package com.getgo.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

/**
 * Page Object Model for Amazon Home Page
 * Contains all elements and actions for the home page
 */
public class AmazonHomePage extends BasePage {
    
    // Locators using AndroidFindBy annotations
    @AndroidFindBy(id = "com.amazon.mShop.android.shopping:id/chrome_search_hint_view")
    private WebElement searchBar;
    
    @AndroidFindBy(id = "com.amazon.mShop.android.shopping:id/action_bar_burger_icon")
    private WebElement menuButton;
    
    @AndroidFindBy(id = "com.amazon.mShop.android.shopping:id/action_bar_cart")
    private WebElement cartButton;
    
    // Dynamic locators
    private By searchBarLocator = By.id("com.amazon.mShop.android.shopping:id/chrome_search_hint_view");
    private By searchBoxLocator = By.id("com.amazon.mShop.android.shopping:id/rs_search_src_text");
    private By menuButtonLocator = By.id("com.amazon.mShop.android.shopping:id/action_bar_burger_icon");
    private By cartButtonLocator = By.id("com.amazon.mShop.android.shopping:id/action_bar_cart");
    private By homepageContainerLocator = AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"gwm-Deck\")");
    private By searchHintLocator = AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"com.amazon.mShop.android.shopping:id/iss_autocomplete_ux_container\")");
    
    // Alternative locators (in case IDs change)
    private By searchBarByText = By.xpath("//*[contains(@text, 'Search Amazon')]");
    private By searchBarByDesc = By.xpath("//*[contains(@content-desc, 'Search')]");
    
    public AmazonHomePage() {
        super();
        logger.info("Amazon Home Page initialized");
    }
    
    /**
     * Click on search bar
     */
    public void clickSearchBar() {
        try {
            if (isElementPresent(searchBarLocator)) {
                click(searchBarLocator);
            } else if (isElementPresent(searchBarByText)) {
                click(searchBarByText);
            } else {
                click(searchBarByDesc);
            }
            logger.info("Clicked on search bar");
        } catch (Exception e) {
            logger.error("Failed to click search bar", e);
            throw e;
        }
    }
    
    /**
     * Search for a product
     * @param productName Name of product to search
     */
    public void searchProduct(String productName) {
        try {
            clickSearchBar();
            waitFor(1);
            sendKeys(searchBoxLocator, productName);
            logger.info("Entered search text: " + productName);
            
            // Press Enter to search - Multiple approaches (in order of preference)
            pressEnterToSearch();
            
            waitFor(2);
            logger.info("Executed search");
        } catch (Exception e) {
            logger.error("Failed to search product", e);
            throw e;
        }
    }
    
    /**
     * Press Enter key to execute search
     * Uses multiple fallback methods for reliability
     */
    private void pressEnterToSearch() {
        boolean searchExecuted = false;
        
//        // Method 1: Press Enter using Keys.ENTER (Most reliable)
//        try {
//            logger.info("Attempting to press Enter using Keys.ENTER");
//            WebElement searchBox = driver.findElement(searchBoxLocator);
//            searchBox.sendKeys(Keys.ENTER);
//            logger.info("Successfully pressed Enter using Keys.ENTER");
//            searchExecuted = true;
//            return;
//        } catch (Exception e) {
//            logger.warn("Method 1 (Keys.ENTER) failed: " + e.getMessage());
//        }
        
        // Method 2: Press Enter using Android KeyEvent (Fallback)
        if (!searchExecuted && driver instanceof AndroidDriver) {
            try {
                logger.info("Attempting to press Enter using Android KeyEvent");
                ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
                logger.info("Successfully pressed Enter using Android KeyEvent");
                searchExecuted = true;
                return;
            } catch (Exception e) {
                logger.warn("Method 2 (Android KeyEvent) failed: " + e.getMessage());
            }
        }
        
        // Method 3: Click search button if present (Fallback)
        if (!searchExecuted) {
            try {
                logger.info("Attempting to click search button as fallback");
                By searchButtonLocator = By.id("com.amazon.mShop.android.shopping:id/rs_search_src_text");
                // Try to find and click a search/submit button
                By submitButton = By.xpath("//*[contains(@content-desc, 'Search') or contains(@text, 'Search')]");
                if (isElementPresent(submitButton)) {
                    click(submitButton);
                    logger.info("Clicked search button");
                    searchExecuted = true;
                    return;
                }
            } catch (Exception e) {
                logger.warn("Method 3 (Search button) failed: " + e.getMessage());
            }
        }
        
        // Method 4: Send newline character (Last resort)
        if (!searchExecuted) {
            try {
                logger.info("Attempting to send newline character");
                WebElement searchBox = driver.findElement(searchBoxLocator);
                searchBox.sendKeys("\n");
                logger.info("Sent newline character");
                searchExecuted = true;
            } catch (Exception e) {
                logger.warn("Method 4 (Newline) failed: " + e.getMessage());
            }
        }
        
        if (!searchExecuted) {
            logger.error("All methods to press Enter failed. Search may not execute.");
        }
    }
    
    /**
     * Verify home page is displayed
     * @return true if home page is displayed
     */
    public boolean isHomePageDisplayed() {
        try {
            boolean displayed = isElementPresent(homepageContainerLocator);
            logger.info("Home page displayed: " + displayed);
            return displayed;
        } catch (Exception e) {
            logger.error("Error verifying home page", e);
            return false;
        }
    }

    /**
     * Verify home page is not present
     * @return true if home page is not present
     */
    public boolean isSearchHintNotPresent() {
        try {
            boolean isPresent = isElementNotPresent(searchHintLocator);
            logger.info("User not in home page : " + isPresent);
            return isPresent;
        } catch (Exception e) {
            logger.error("Error verifying home page", e);
            return false;
        }
    }
    
}
