@AmazonShopping @Verification
Feature: Amazon Element Verification
  As a user of Amazon Shopping app
  I want to verify elements are present or not present
  So that I can ensure the app is working correctly

  Background:
    Given the Amazon app is launched successfully
    And I am on the home page

  @ElementPresence @Smoke
  Scenario: Verify elements are present on home page
    Given I am on the Amazon home page
    Then the search bar should be present

  @ElementNotPresence
  Scenario: Verify element is not present after search
    Given I am on the Amazon home page
    When I search for "Samsung galaxy S25 Ultra" in the search bar
    Then the home page search hint should not be present
    And I should see search results displayed
