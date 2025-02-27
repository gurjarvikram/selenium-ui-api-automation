
@tag
Feature: Purchase the order from Ecommerce Website
  I want to use this template for my feature file
	
	Background:  
	Given I landed on Ecommerce page

  @Regression
  Scenario Outline: Positive Test of submitting the order
    Given I logged in with username <name> and password <password>
    When I add product <productName> to cart
    And I checkout <productName> and sumit the order
    Then "Thankyou for the order." message is displayed on confirmation page

    Examples: 
      | name		  						| password 		| productName  |
      | ***REDACTED-EMAIL*** 		| ***REDACTED-PASSWORD*** 	| ZARA COAT 3  |