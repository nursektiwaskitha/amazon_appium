@AmazonShopping @Filter
Feature: Amazon Filter Functionality
  As a user of Amazon Shopping app
  I want to apply filters to search results
  So that I can find products that meet my criteria

  Background:
    Given the Amazon app is launched successfully
    And I am on the home page

  @Smoke @Filter
  Scenario: Complete end-to-end shopping flow with all features
    Given I am on the Amazon home page
    When I search for "iPhone 17 Pro Max" in the search bar
    Then the home page search hint should not be present
    And I should see search results displayed
    When I capture the product image from search results
    And I apply the "4 star and above" filter
    Then all products should have rating of 4.0 stars or above