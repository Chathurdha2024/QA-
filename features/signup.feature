Feature: User Signup
  As a new user
  I want to register
  So that I can create an account

  Scenario: Successful signup with valid input
    When I signup with name "Hasangi", email "newuser@example.com" and password "Password123"
    Then the signup should be successful

  Scenario: Signup fails with invalid email
    When I signup with name "Hasangi", email "bad-email" and password "Password123"
    Then I should see a signup failure

  Scenario: Signup fails with missing name
    When I signup with name "", email "user2@example.com" and password "Password123"
    Then I should see a signup failure

  Scenario: Signup fails with missing password
    When I signup with name "Hasangi", email "user3@example.com" and password ""
    Then I should see a signup failure