Feature: FanCode User Todo Completion

  @critical
  Scenario: All the users of City `FanCode` should have more than half of their todos task completed.
    Given User has the todo tasks
    And  User belongs to the city "FanCode" with lat between -40 and 5 and long between 5 and 100
    Then User Completed task percentage should be greater than 50%