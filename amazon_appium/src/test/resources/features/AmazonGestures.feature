@AmazonShopping @Gesture
Feature: Amazon App Gesture Controls
  As a user of Amazon Shopping app
  I want to use various gestures
  So that I can interact naturally with the app

  Background:
    Given the Amazon app is launched successfully
    And I am on the home page

  @Swipe
  Scenario: Complete end-to-end shopping flow with all features
    Given I am on the Amazon home page
    When I search for "iPhone 17 Pro Max" in the search bar
    Then I should see search results displayed
    When I click on the first product from search results
    Then the product details page should be displayed
    And I should see the product title
    When I swipe the product image to the left
    Then the next product image should be displayed

  @ScrollUntilElement
  Scenario: Complete end-to-end shopping flow with all features
    Given I am on the Amazon home page
    When I search for "iPhone 17 Pro Max" in the search bar
    Then I should see search results displayed
    When I click on the first product from search results
    Then the product details page should be displayed
    And I should see the product title
    When I scroll until price is visible
    Then I should see the product price

  @Drag
  Scenario: Complete end-to-end shopping flow with all features
    Given I am on the Amazon home page
    When I search for "iPhone 17 Pro Max" in the search bar
    Then the home page search hint should not be present
    And I should see search results displayed
    When I swipe suggested filter
    Then some suggested filters should not be visible anymore

  @Zoom
  Scenario: Complete end-to-end shopping flow with all features
    Given I am on the Amazon home page
    When I search for "iPhone 17 Pro Max" in the search bar
    Then the home page search hint should not be present
    And I should see search results displayed
    When I click on the first product from search results
    Then the product details page should be displayed
    And I should see the product title
    When I zoom in on the product image
    Then the product image should be enlarged
    When I zoom out on the product image
    Then the product image should return to normal size