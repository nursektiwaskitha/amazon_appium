@AmazonShopping @Navigation
Feature: Amazon App Navigation and Routing
  As a user of Amazon Shopping app
  I want to navigate between different pages
  So that I can browse products efficiently

  Background:
    Given the Amazon app is launched successfully
    And I am on the home page

  @Routing @Smoke
  Scenario: Navigate between pages - Home to Search to Product
    Given I am on the Amazon home page
    When I search for "iPhone 17 Pro Max" in the search bar
    Then the home page search hint should not be present
    And I should see search results displayed
    When I click on the first product
    Then the product details page should be displayed
    When I navigate back
    Then I should see search results displayed
    When I navigate back
    Then I should be back to the home page
