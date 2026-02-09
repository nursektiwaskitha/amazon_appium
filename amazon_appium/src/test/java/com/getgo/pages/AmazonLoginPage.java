package com.getgo.pages;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Page Object Model for Amazon Login Page
 * Contains all elements and actions for the login flow
 */
public class AmazonLoginPage extends BasePage {
    
    // Locators for login page elements
    private By accountMenuLocator =  AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"com.amazon.mShop.android.shopping:id/bottom_tab_button_icon\").instance(1)");
    private By signInButtonLocator = AppiumBy.accessibilityId("Sign in");
    private By emailInputLocator = AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"ap_email_login\")");
    private By continueButtonLocator = AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"continue\")");
    private By passwordInputLocator = AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"ap_password\")");
    private By submitSignInButtonLocator = AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"auth-signin-button\")");
    private By loggedInIndicatorLocator = AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"me\")");
    private By meButtonLocator = AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"user_avatar\")");
    private By logoutButtonLocator = AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"sign-out-button\")");
    private By signOutButtonInModalLocator = AppiumBy.androidUIAutomator("new UiSelector().text(\"SIGN OUT\")");
    private By skipSignInLocator = By.id("com.amazon.mShop.android.shopping:id/skip_sign_in_button");
    private By homepageContainerLocator = AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"gwm-Deck\")");

    public AmazonLoginPage() {
        super();
        logger.info("Amazon Login Page initialized");
    }
    
    /**
     * Click on the account menu to start login flow
     */
    public void clickAccountMenu() {
        logger.info("Clicking on account menu");
        click(accountMenuLocator);
        waitForEitherElement(signInButtonLocator, meButtonLocator, 15);
        logger.info("Account menu clicked");
    }
    
    /**
     * Click on Sign In button
     */
    public void clickSignInButton() {
        logger.info("Clicking on Sign In button");
        click(signInButtonLocator);
        waitForElementToBeVisible(emailInputLocator, 15);
        logger.info("Sign In button clicked");
    }
    
    /**
     * Click on any button by text
     * @param buttonText The text of the button to click
     */
    public void clickButton(String buttonText) {
        logger.info("Clicking on '{}' button", buttonText);
        By buttonLocator = By.xpath("//*[contains(@text, '" + buttonText + "') or contains(@content-desc, '" + buttonText + "')]");
        click(buttonLocator);
        waitFor(2);
        logger.info("'{}' button clicked", buttonText);
    }
    
    /**
     * Enter email address
     * @param email The email address to enter
     */
    public void enterEmail(String email) {
        logger.info("Entering email: {}", email);
        WebElement emailField = driver.findElement(emailInputLocator);
        emailField.clear();
        sendKeys(emailInputLocator, email);
        logger.info("Email entered successfully");
    }
    
    /**
     * Click on Continue button after entering email
     */
    public void clickContinueButton() {
        logger.info("Clicking on Continue button");
        click(continueButtonLocator);
        waitForElementToBeVisible(passwordInputLocator, 15);
        logger.info("Continue button clicked");
    }
    
    /**
     * Enter password
     * @param password The password to enter
     */
    public void enterPassword(String password) {
        logger.info("Entering password");
        WebElement passwordField = driver.findElement(passwordInputLocator);
        passwordField.clear();
        sendKeys(passwordInputLocator, password);
        logger.info("Password entered successfully");
    }
    
    /**
     * Click on final Sign In button to submit credentials
     */
    public void clickSubmitSignInButton() {
        logger.info("Clicking on submit Sign In button");
        click(submitSignInButtonLocator);
        waitForElementToBeVisible(loggedInIndicatorLocator, 10);
        logger.info("Submit Sign In button clicked");
    }
    
    /**
     * Complete login flow with email and password
     * @param email The email address
     * @param password The password
     */
    public void login(String email, String password) {
        logger.info("Starting login flow for email: {}", email);
        clickAccountMenu();
        clickSignInButton();
        enterEmail(email);
        clickContinueButton();
        enterPassword(password);
        clickSubmitSignInButton();
        logger.info("Login flow completed");
    }
    
    /**
     * Verify if user is logged in
     * @return true if logged in indicator is present
     */
    public boolean isLoggedIn() {
        logger.info("Checking if user is logged in");
        clickAccountMenu();
        boolean loggedIn = isElementPresent(loggedInIndicatorLocator);
        logger.info("User logged in status: {}", loggedIn);
        return loggedIn;
    }
    
    /**
     * Verify if account name is visible
     * @return true if account name/greeting is visible
     */
    public boolean isAccountNameVisible() {
        logger.info("Checking if account name is visible");
        boolean visible = isElementPresent(loggedInIndicatorLocator);
        logger.info("Account name visible: {}", visible);
        return visible;
    }
    
    /**
     * Get the logged in user's greeting text
     * @return The greeting text (e.g., "Hello, User")
     */
    public String getLoggedInUserGreeting() {
        logger.info("Getting logged in user greeting");
        try {
            WebElement greetingElement = driver.findElement(loggedInIndicatorLocator);
            String greeting = greetingElement.getText();
            logger.info("User greeting: {}", greeting);
            return greeting;
        } catch (Exception e) {
            logger.error("Failed to get user greeting", e);
            return "";
        }
    }
    
    /**
     * Logout from Amazon account
     */
    public void logout() {
        logger.info("Attempting to logout");
        try {
            // Go to account page
            clickAccountMenu();
            
            // Look for and click logout/sign out button
            click(meButtonLocator);
            waitForElementToBeVisible(logoutButtonLocator, 15);

            click(logoutButtonLocator);
            waitForElementToBeVisible(signOutButtonInModalLocator, 15);

            click(signOutButtonInModalLocator);
            waitForElementToBeVisible(skipSignInLocator, 15);

            click(skipSignInLocator);
            waitForElementToBeVisible(homepageContainerLocator, 15);
            
        } catch (Exception e) {
            logger.error("Failed to logout", e);
        }
    }
    
    /**
     * Logout if user is currently logged in
     * @return true if logout was performed, false otherwise
     */
    public boolean logoutIfLoggedIn() {
        logger.info("Checking if user is logged in to perform logout");
        try {
            waitForElementToBeVisible(homepageContainerLocator, 15);
            if (isLoggedIn()) {
                logger.info("User is logged in, performing logout");
                logout();
                return true;
            } else {
                logger.info("User is not logged in, skipping logout");
                return false;
            }
        } catch (Exception e) {
            logger.error("Error while checking login status", e);
            return false;
        }
    }
}
