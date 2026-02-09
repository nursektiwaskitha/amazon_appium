@AmazonShopping @Authentication
Feature: Amazon Login and Authentication
  As a user of Amazon Shopping app
  I want to login to my account
  So that I can access personalized features

  Background:
    Given the Amazon app is launched successfully
    And I am on the home page

  @Login @Smoke
  Scenario: Login to Amazon account
    Given I am on the Amazon home page
    When I click on the account menu
    And I click on sign in button
    And I enter email "testsekti@gmail.com"
    And I click on continue button
    And I enter password "TestSekti99!!"
    And I submit login account
    Then I should be logged in successfully
    And I should see my account name
