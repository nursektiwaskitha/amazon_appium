@AmazonShopping @ProductDetails
Feature: Amazon Product Details Page
  As a user of Amazon Shopping app
  I want to view product details
  So that I can make informed purchasing decisions

  Background:
    Given the Amazon app is launched successfully
    And I am on the home page

  @Click @Smoke
  Scenario: Navigate to product details page
    Given I am on the Amazon home page
    When I search for "iPhone 17 Pro Max" in the search bar
    And I click on the first product from search results
    Then the product details page should be displayed
    And I should see the product title

  @Scroll
  Scenario: Scroll to view product price
    Given I am on the Amazon home page
    When I search for "iPhone 17 Pro Max" in the search bar
    And I click on the first product from search results
    Then the product details page should be displayed
    When I scroll until price is visible
    Then I should see the product price

