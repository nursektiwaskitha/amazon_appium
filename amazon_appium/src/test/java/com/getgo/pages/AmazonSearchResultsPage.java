package com.getgo.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object Model for Amazon Search Results Page
 */
public class AmazonSearchResultsPage extends BasePage {
    
    @AndroidFindBy(id = "com.amazon.mShop.android.shopping:id/rs_results_filter_bar")
    private WebElement filterBar;
    
    @AndroidFindBy(id = "com.amazon.mShop.android.shopping:id/rs_results_sort_spinner")
    private WebElement sortSpinner;

    // Dynamic locators
    private By filterBarLocator = By.id("com.amazon.mShop.android.shopping:id/rs_results_filter_bar");
    private By sortSpinnerLocator = By.id("com.amazon.mShop.android.shopping:id/rs_results_sort_spinner");
    private By searchContainerLocator = AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"search\")");
    private By productTitlesLocator = By.id("com.amazon.mShop.android.shopping:id/item_title");
    private By filterButtonLocator = By.xpath("//*[contains(@text, 'Filter')]");
    private By suggestedFilterBar = AppiumBy.androidUIAutomator("new UiSelector().text(\"Global refinements\")");
    private By amazonsChoiceProductLocator = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.view.ViewGroup\").childSelector(new UiSelector().description(\"Amazon's Choice\"))");
    
    public AmazonSearchResultsPage() {
        super();
        logger.info("Amazon Search Results Page initialized");
    }
    
    /**
     * Verify search results page is displayed
     * @return true if displayed
     */
    public boolean isSearchResultsPageDisplayed() {
        try {
            waitForElementToBeVisible(searchContainerLocator, 15);
            // Get the search container element
            WebElement searchContainer = driver.findElement(searchContainerLocator);
            
            // Find all android.view.View elements within the search container
            List<WebElement> viewElements = searchContainer.findElements(By.className("android.view.View"));
            
            // Filter to get only those View elements that have a resource-id attribute
            // These represent individual product items
            long productCount = viewElements.stream()
                .filter(element -> {
                    try {
                        String resourceId = element.getAttribute("resource-id");
                        return resourceId != null && !resourceId.isEmpty() && !resourceId.equals("search");
                    } catch (Exception e) {
                        return false;
                    }
                })
                .count();
            
            if (productCount > 0) {
                logger.info("Search results page displayed with " + productCount + " product view(s)");
                return true;
            }
            
            // Fallback: check for filter bar
            boolean displayed = isElementPresent(filterBarLocator);
            logger.info("Search results page displayed (via filter bar): " + displayed);
            return displayed;
        } catch (Exception e) {
            logger.error("Error verifying search results page", e);
            return false;
        }
    }
    
    /**
     * Verify that search results container is not empty
     * @return true if search results contain product elements (not just "No results" message)
     */
    /**
     * Click on first product in search results
     */
    public void clickFirstProduct() {
        try {
            waitForElementToBeVisible(searchContainerLocator, 15);

            // Find the results ListView
            WebElement searchContainer = driver.findElement(searchContainerLocator);
            
            // Find all product cards within the results
            // Product cards are clickable View elements with non-empty content-desc
            List<WebElement> allViews = searchContainer.findElements(By.className("android.view.View"));
            
            logger.info("Found {} View elements in search results", allViews.size());
            
            // Filter to get only clickable product cards with content-desc
            List<WebElement> productCards = new ArrayList<>();
            for (WebElement view : allViews) {
                try {
                    String contentDesc = view.getAttribute("content-desc");
                    String clickable = view.getAttribute("clickable");
                    
                    // Product cards are clickable and have meaningful content-desc
                    if ("true".equals(clickable) && contentDesc != null && !contentDesc.isEmpty() 
                        && !contentDesc.contains("filter") && !contentDesc.contains("Button")) {
                        productCards.add(view);
                        logger.info("Found product card: {}", contentDesc.substring(0, Math.min(50, contentDesc.length())));
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            
            logger.info("Found {} clickable product cards", productCards.size());
            
            // Click the first product card
            if (!productCards.isEmpty()) {
                WebElement firstProduct = productCards.get(0);
                String productDesc = firstProduct.getAttribute("content-desc");
                click(firstProduct);
                logger.info("Clicked first product card: {}", productDesc.substring(0, Math.min(50, productDesc.length())));
                waitFor(2);
            } else {
                logger.error("No product cards found in search results");
                throw new RuntimeException("No product cards found");
            }
            
        } catch (NoSuchElementException e) {
            logger.error("Search results container not found", e);
            throw new RuntimeException("Search results not found", e);
        } catch (Exception e) {
            logger.error("Failed to click first product", e);
            throw e;
        }
    }
    
    /**
     * Open filter options
     */
    public void openFilters() {
        try {
            if (isElementPresent(filterButtonLocator)) {
                click(filterButtonLocator);
            } else if (isElementPresent(filterBarLocator)) {
                click(filterBarLocator);
            }
            logger.info("Opened filters");
            waitFor(1);
        } catch (Exception e) {
            logger.error("Failed to open filters", e);
            throw e;
        }
    }
    
    /**
     * Get number of products in results
     * @return Number of products
     */
    public int getProductCount() {
        try {
            // Get the search container element
            WebElement searchContainer = driver.findElement(searchContainerLocator);
            
            // Find ListView elements within the search container
            List<WebElement> listViews = searchContainer.findElements(By.className("android.widget.ListView"));
            
            // Count all clickable product elements
            int count = 0;
            
            // Filter ListViews that contain "Add to cart" button
            for (WebElement listView : listViews) {
                try {
                    // Check if this ListView contains an "Add to cart" button
                    By addToCartButtonLocator = AppiumBy.androidUIAutomator(
                        "new UiSelector().className(\"android.widget.Button\").textContains(\"Add to cart\")"
                    );
                    
                    List<WebElement> addToCartButtons = listView.findElements(addToCartButtonLocator);
                    
                    // If this ListView doesn't have "Add to cart" button, skip it
                    if (addToCartButtons.isEmpty()) {
                        continue;
                    }
                    
                    // Use UiAutomator to find View elements that have content-desc attribute
                    By viewWithContentDescLocator = AppiumBy.androidUIAutomator(
                        "new UiSelector().className(\"android.view.View\").descriptionMatches(\".+\")"
                    );
                    
                    List<WebElement> productViews = listView.findElements(viewWithContentDescLocator);
                    
                    // Count clickable products
                    for (WebElement view : productViews) {
                        try {
                            boolean isClickable = Boolean.parseBoolean(view.getAttribute("clickable"));
                            
                            if (isClickable) {
                                count++;
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            
            logger.info("Product count: " + count);
            return count;
        } catch (Exception e) {
            logger.error("Failed to get product count", e);
            return 0;
        }
    }
    
    /**
     * Scroll through search results
     */
    public void scrollResults() {
        swipeUp();
        logger.info("Scrolled through search results");
        waitFor(1);
    }

    /**
     * Swipe horizontally suggested filter
     */
    /**
     * Get visible filter items in the suggested filter bar
     * @return List of visible filter text/content-desc
     */
    public List<String> getVisibleSuggestedFilters() {
        try {
            WebElement suggestedFilterBarElement = driver.findElement(suggestedFilterBar);
            List<WebElement> filterViews = suggestedFilterBarElement.findElements(By.className("android.view.View"));
            
            List<String> visibleFilters = new ArrayList<>();
            for (WebElement view : filterViews) {
                try {
                    // Check if element is displayed
                    if (view.isDisplayed()) {
                        String filterLabel = view.getAttribute("text");
                        
                        if (filterLabel != null) {
                            visibleFilters.add(filterLabel);
                            logger.info("Found visible filter: " + filterLabel);
                        }
                    }
                } catch (Exception e) {
                    // Skip elements that can't be accessed
                    continue;
                }
            }
            
            logger.info("Total visible filters found: " + visibleFilters.size());
            return visibleFilters;
            
        } catch (Exception e) {
            logger.error("Failed to get visible suggested filters", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Capture screenshot of Amazon's Choice product image from search results
     * @param outputPath Path to save the screenshot
     * @return true if screenshot was captured successfully
     */
    public boolean captureAmazonsChoiceProductImage(String outputPath) {
        try {
            if (isElementPresent(amazonsChoiceProductLocator)) {
                // Find the product card container that has Amazon's Choice badge
                WebElement productCard = driver.findElement(amazonsChoiceProductLocator);
                
                // Try to find ImageView elements within the product card
                List<WebElement> images = productCard.findElements(By.className("android.widget.ImageView"));
                
                WebElement imageToCapture;
                if (!images.isEmpty()) {
                    // Capture the first ImageView (usually the main product image)
                    imageToCapture = images.get(0);
                    logger.info("Found product image (ImageView) in Amazon's Choice product card");
                } else {
                    // Fallback: capture the entire product card if no specific ImageView found
                    imageToCapture = productCard;
                    logger.info("Capturing entire product card as no ImageView found");
                }
                
                File screenshot = imageToCapture.getScreenshotAs(org.openqa.selenium.OutputType.FILE);
                File outputFile = new File(outputPath);
                org.apache.commons.io.FileUtils.copyFile(screenshot, outputFile);
                
                logger.info("Captured Amazon's Choice product image: " + outputPath);
                return true;
            } else {
                logger.warn("Amazon's Choice product not found in search results");
                return false;
            }
        } catch (Exception e) {
            logger.error("Failed to capture Amazon's Choice product image", e);
            return false;
        }
    }
    
    /**
     * Apply 4 star and above filter from suggested filter bar
     * Looks for filter button with text containing "4"
     */
    public void applyFourStarFilter() {
        try {
            // Find filter button with text containing "4" (e.g., "4 & Up", "4 Stars & Up", etc.)
            By fourStarFilterLocator = AppiumBy.androidUIAutomator(
                "new UiSelector().textMatches(\".*4.*[Ss]tar.*|.*4.*[Uu]p.*\")"
            );
            
            if (isElementPresent(fourStarFilterLocator)) {
                WebElement fourStarFilter = driver.findElement(fourStarFilterLocator);
                String filterText = fourStarFilter.getText();
                click(fourStarFilter);
                logger.info("Applied 4 star filter: " + filterText);
                waitFor(2); // Wait for results to refresh
            } else {
                logger.warn("4 star filter not found in suggested filters");
            }
        } catch (Exception e) {
            logger.error("Failed to apply 4 star filter", e);
            throw e;
        }
    }
    
    /**
     * Verify all products have star rating >= 4.0
     * Checks products that have star rating text displayed
     * @return true if all products with visible ratings have >= 4.0 stars
     */
    public boolean verifyAllProductsHaveMinStars(double minStars) {
        try {
            // Find all TextView elements with text containing "out of 5 stars"
            By starRatingLocator = AppiumBy.androidUIAutomator(
                "new UiSelector().className(\"android.widget.TextView\").textMatches(\".*out of 5 stars.*\")"
            );
            
            List<WebElement> ratingElements = driver.findElements(starRatingLocator);
            logger.info("Found " + ratingElements.size() + " products with star ratings");
            
            if (ratingElements.isEmpty()) {
                logger.warn("No products with star ratings found");
                return false;
            }
            
            int validProducts = 0;
            int invalidProducts = 0;
            
            for (WebElement ratingElement : ratingElements) {
                try {
                    String ratingText = ratingElement.getText();
                    logger.info("Checking rating: " + ratingText);
                    
                    // Extract rating number (e.g., "4.6 out of 5 stars" -> 4.6)
                    String[] parts = ratingText.split(" ");
                    if (parts.length > 0) {
                        String ratingStr = parts[0];
                        double rating = Double.parseDouble(ratingStr);
                        
                        if (rating >= minStars) {
                            validProducts++;
                            logger.info("✓ Product rating " + rating + " is >= " + minStars);
                        } else {
                            invalidProducts++;
                            logger.error("✗ Product rating " + rating + " is < " + minStars);
                        }
                    }
                } catch (Exception e) {
                    logger.warn("Could not parse rating from element: " + e.getMessage());
                }
            }
            
            logger.info("Verification complete - Valid: " + validProducts + ", Invalid: " + invalidProducts);
            
            return invalidProducts == 0 && validProducts > 0;
            
        } catch (Exception e) {
            logger.error("Failed to verify product star ratings", e);
            return false;
        }
    }
    
    /**
     * Swipe suggested filter bar to the left 3 times
     */
    public void swipeSuggestedFilter() {
        logger.info("Swipe left on suggested filter 3 times");
        try {
            WebElement suggestedFilterBarElement = driver.findElement(suggestedFilterBar);
            
            for (int i = 1; i <= 3; i++) {
                logger.info("Performing swipe left #" + i);
                swipeLeftOnElement(suggestedFilterBarElement);
                waitFor(1); // Wait a moment between swipes
            }
            
            logger.info("Completed 3 swipes on suggested filter");
        } catch (Exception e) {
            logger.error("Failed to swipe suggested filter", e);
            throw e;
        }
    }
    
    /**
     * Verify that some filters are no longer visible after swiping
     * @param filtersBeforeSwipe List of filters before swiping
     * @return true if at least one filter is no longer visible
     */
    public boolean verifyFiltersChangedAfterSwipe(List<String> filtersBeforeSwipe) {
        try {
            // Wait a moment for UI to stabilize after swipe
            waitFor(1);
            
            List<String> filtersAfterSwipe = getVisibleSuggestedFilters();
            
            logger.info("Filters before swipe: " + filtersBeforeSwipe);
            logger.info("Filters after swipe: " + filtersAfterSwipe);
            
            // Check if at least one filter from before is no longer visible
            for (String filterBefore : filtersBeforeSwipe) {
                if (!filtersAfterSwipe.contains(filterBefore)) {
                    logger.info("Filter '" + filterBefore + "' is no longer visible after swipe");
                    return true;
                }
            }
            
            // Also check if new filters appeared (were not visible before)
            for (String filterAfter : filtersAfterSwipe) {
                if (!filtersBeforeSwipe.contains(filterAfter)) {
                    logger.info("New filter '" + filterAfter + "' is now visible after swipe");
                    return true;
                }
            }
            
            logger.warn("No change detected in visible filters after swipe");
            return false;
            
        } catch (Exception e) {
            logger.error("Failed to verify filters changed after swipe", e);
            return false;
        }
    }
}
