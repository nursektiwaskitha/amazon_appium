@AmazonShopping @Search
Feature: Amazon Search Functionality
  As a user of Amazon Shopping app
  I want to search for products
  So that I can find items I want to purchase

  Background:
    Given the Amazon app is launched successfully
    And I am on the home page

  @Smoke
  Scenario: Search for a product and verify results
    Given I am on the Amazon home page
    When I search for "iPhone 17 Pro Max" in the search bar
    Then I should see search results displayed
    And search results should contain products
