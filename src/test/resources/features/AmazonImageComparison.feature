@AmazonShopping @ImageComparison
Feature: Amazon Image Comparison
  As a user of Amazon Shopping app
  I want to compare product images
  So that I can verify image consistency and quality

  Background:
    Given the Amazon app is launched successfully
    And I am on the home page

  @Verification
  Scenario: Complete end-to-end shopping flow with all features
    Given I am on the Amazon home page
    When I search for "iPhone 17 Pro Max" in the search bar
    Then the home page search hint should not be present
    And I should see search results displayed
    When I capture the product image from search results
    When I click on the first product from search results
    Then the product details page should be displayed
    And I should see the product title
    When I capture the product image from product details
    Then the product images should match with 80-100% similarity