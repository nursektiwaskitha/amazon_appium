@AmazonShopping @E2E
Feature: Amazon End-to-End Shopping Flow
  As a user of Amazon Shopping app
  I want to perform a complete shopping flow
  So that I can verify all functionalities work together seamlessly

  Background:
    Given the Amazon app is launched successfully
    And I am on the home page

  @Smoke @Comprehensive
  Scenario: Complete end-to-end shopping flow with all features
    Given I am on the Amazon home page
    When I search for "iPhone 17 Pro Max" in the search bar
    Then the home page search hint should not be present
    And I should see search results displayed
    When I capture the product image from search results
    And I apply the "4 star and above" filter
    Then all products should have rating of 4.0 stars or above
    When I swipe suggested filter
    Then some suggested filters should not be visible anymore
    When I click on the first product from search results
    Then the product details page should be displayed
    And I should see the product title
    When I capture the product image from product details
    Then the product images should match with 80-100% similarity
    When I swipe the product image to the left
    Then the next product image should be displayed
    When I zoom in on the product image
    Then the product image should be enlarged
    When I zoom out on the product image
    Then the product image should return to normal size
    When I scroll until price is visible
    Then I should see the product price
