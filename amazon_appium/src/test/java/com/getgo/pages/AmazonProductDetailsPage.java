package com.getgo.pages;

import com.getgo.utils.GestureUtil;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.List;

/**
 * Page Object Model for Amazon Product Details Page
 */
public class AmazonProductDetailsPage extends BasePage {
    
    @AndroidFindBy(id = "com.amazon.mShop.android.shopping:id/add_to_cart_button")
    private WebElement addToCartButton;
    
    @AndroidFindBy(id = "com.amazon.mShop.android.shopping:id/buy_now_button")
    private WebElement buyNowButton;
    
    // Dynamic locators
    private By productTitleLocator = AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"title_feature_div\")");
    private By productPriceLocator = AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"corePriceDisplay_mobile_feature_div\")");
    private By addToCartLocator = By.id("com.amazon.mShop.android.shopping:id/add_to_cart_button");
    private By buyNowLocator = By.id("com.amazon.mShop.android.shopping:id/buy_now_button");
    private By productImageLocator = AppiumBy.androidUIAutomator("new UiSelector().resourceIdMatches(\".*image-block-product-image.*\")");
    private By productImageRowLocator = AppiumBy.androidUIAutomator("new UiSelector().resourceIdMatches(\"image-block-row\")");
    private By quantitySpinnerLocator = By.id("com.amazon.mShop.android.shopping:id/quantity");
    
    // Alternative locators
    private By addToCartByText = By.xpath("//*[contains(@text, 'Add to Cart')]");
    private By buyNowByText = By.xpath("//*[contains(@text, 'Buy Now')]");
    
    public AmazonProductDetailsPage() {
        super();
        logger.info("Amazon Product Details Page initialized");
    }
    
    /**
     * Verify product details page is displayed
     * @return true if displayed
     */
    public boolean isProductDetailsPageDisplayed() {
        try {
            boolean displayed = isElementPresent(productTitleLocator);
            logger.info("Product details page displayed: " + displayed);
            return displayed;
        } catch (Exception e) {
            logger.error("Error verifying product details page", e);
            return false;
        }
    }
    
    /**
     * Get product title
     * @return Product title text
     */
    public String getProductTitle() {
        try {
            WebElement productTitleContainer = driver.findElement(productTitleLocator);
            WebElement titleElement = productTitleContainer.findElement(By.className("android.view.View"));
            if (isElementPresent(productTitleLocator)) {
                return titleElement.getText();
            }
            return "";
        } catch (Exception e) {
            logger.error("Failed to get product title", e);
            return "";
        }
    }
    
    /**
     * Get product price
     * @return Product price text
     */
    public String getProductPrice() {
        try {
            if (isElementPresent(productPriceLocator)) {
                WebElement priceLabelContainer = driver.findElement(productPriceLocator);
                WebElement priceLabel = priceLabelContainer.findElement(By.className("android.view.View/android.widget.TextView"));
                return priceLabel.getText();
            }
            return "";
        } catch (Exception e) {
            logger.error("Failed to get product price", e);
            return "";
        }
    }

    /**
     * Scroll to price label
     */
    public void scrollUntilProductPrice() {
        try {
            // Scroll until the price element becomes visible
            int maxScrolls = 10;
            int scrollAttempts = 0;
            
            while (scrollAttempts < maxScrolls) {
                // Check if price element is present by resource-id
                if (isElementPresent(productPriceLocator)) {
                    logger.info("Price element found by resource-id after " + scrollAttempts + " scroll(s)");
                    // Scroll to the element to ensure it's fully visible
                    WebElement priceLabelContainer = driver.findElement(productPriceLocator);
                    scrollToElement(priceLabelContainer, 2);
                    return;
                }
                
                // Fallback: Check by text (price usually contains $)
                By priceByTextLocator = AppiumBy.androidUIAutomator(
                    "new UiSelector().textMatches(\".*\\$.*\")"
                );
                if (isElementPresent(priceByTextLocator)) {
                    logger.info("Price element found by text pattern after " + scrollAttempts + " scroll(s)");
                    WebElement priceElement = driver.findElement(priceByTextLocator);
                    scrollToElement(priceElement, 2);
                    return;
                }
                
                // Scroll up to reveal more content
                logger.info("Price element not found, scrolling up (attempt " + (scrollAttempts + 1) + ")");
                swipeUp();
                waitFor(1);
                scrollAttempts++;
            }
            
            logger.warn("Price element not found after " + maxScrolls + " scroll attempts");
        } catch (Exception e) {
            logger.error("Failed to scroll until price label", e);
            throw e;
        }
    }
    
    /**
     * Zoom in on product image
     */
    public void zoomProductImage() {
        try {
            List<WebElement> productImageList = driver.findElements(productImageLocator);
            if (isElementPresent(productImageLocator) && !productImageList.isEmpty()) {
                GestureUtil.zoomIn(productImageList.get(0));
                logger.info("Zoomed in on product image");
                waitFor(1);
            }
        } catch (Exception e) {
            logger.error("Failed to zoom product image", e);
            throw e;
        }
    }
    
    /**
     * Zoom out on product image
     */
    public void zoomOutProductImage() {
        try {
            List<WebElement> productImageList = driver.findElements(productImageLocator);
            if (isElementPresent(productImageLocator) && !productImageList.isEmpty()) {
                GestureUtil.zoomOut(productImageList.get(0));
                logger.info("Zoomed out on product image");
                waitFor(1);
            }
        } catch (Exception e) {
            logger.error("Failed to zoom out product image", e);
            throw e;
        }
    }
    
    /**
     * Get product image dimensions (width x height)
     * @return Dimension object with width and height
     */
    public org.openqa.selenium.Dimension getProductImageSize() {
        try {
            List<WebElement> productImageList = driver.findElements(productImageLocator);
            if (!productImageList.isEmpty()) {
                org.openqa.selenium.Dimension size = productImageList.get(0).getSize();
                logger.info("Product image size: " + size.getWidth() + "x" + size.getHeight());
                return size;
            }
            logger.warn("No product image found");
            return null;
        } catch (Exception e) {
            logger.error("Failed to get product image size", e);
            return null;
        }
    }
    
    /**
     * Calculate area of the product image
     * @return Area (width * height)
     */
    public int getProductImageArea() {
        try {
            org.openqa.selenium.Dimension size = getProductImageSize();
            if (size != null) {
                int area = size.getWidth() * size.getHeight();
                logger.info("Product image area: " + area + " pixels");
                return area;
            }
            return 0;
        } catch (Exception e) {
            logger.error("Failed to calculate product image area", e);
            return 0;
        }
    }
    
    /**
     * Capture screenshot of the product image
     * @param outputPath Path to save the screenshot
     * @return true if screenshot was captured successfully
     */
    public boolean captureProductImage(String outputPath) {
        try {
            List<WebElement> productImageList = driver.findElements(productImageLocator);
            
            if (!productImageList.isEmpty()) {
                WebElement productImage = productImageList.get(0);
                File screenshot = productImage.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
                
                File outputFile = new File(outputPath);
                org.apache.commons.io.FileUtils.copyFile(screenshot, outputFile);
                
                logger.info("Captured product image: " + outputPath);
                return true;
            } else {
                logger.warn("Product image not found");
                return false;
            }
        } catch (Exception e) {
            logger.error("Failed to capture product image", e);
            return false;
        }
    }
    
    /**
     * Get current product image index by checking resource-id
     * @return Current image index (e.g., 0, 1, 2) or -1 if not found
     */
    public int getCurrentProductImageIndex() {
        try {
            waitForElementToBeVisible(productImageLocator, 15);
            List<WebElement> productImageList = driver.findElements(productImageLocator);
            
            for (WebElement image : productImageList) {
                try {
                    String resourceId = image.getAttribute("resource-id");
                    logger.info("resourceId" + resourceId);
                    if (resourceId != null && resourceId.contains("image-block-product-image-")) {
                        // Extract index from resource-id like "image-block-product-image-0"
                        String[] parts = resourceId.split("-");
                        if (parts.length > 0) {
                            String indexStr = parts[parts.length - 1];
                            int index = Integer.parseInt(indexStr);
                            logger.info("Current product image index: " + index + " (resource-id: " + resourceId + ")");
                            return index;
                        }
                    }
                } catch (Exception e) {
                    logger.warn("Could not parse index from image element: " + e.getMessage());
                }
            }
            
            logger.warn("No product image with index found");
            return -1;
            
        } catch (Exception e) {
            logger.error("Failed to get current product image index", e);
            return -1;
        }
    }
    
    /**
     * Swipe product image to the left (next image)
     */
    public void swipeProductImageLeft() {
        try {
            WebElement productImageRow = driver.findElement(productImageRowLocator);

            swipeLeftOnElement(productImageRow);
            logger.info("Swiped product image to the left");
        } catch (Exception e) {
            logger.error("Failed to swipe product image", e);
            throw e;
        }
    }
    
    /**
     * Swipe through product images
     */
    public void swipeProductImages() {
        swipeLeft();
        logger.info("Swiped through product images");
        waitFor(1);
    }
    
    /**
     * Scroll to reviews section
     */
    public void scrollToReviews() {
        scrollToElementByText("Customer reviews", 5);
        logger.info("Scrolled to reviews section");
    }
    
}
