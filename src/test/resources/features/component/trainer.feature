Feature: Trainer Workload Management

  Scenario: Create new trainer summary from workload
    Given a workload event with username "john.doe"
    When the workload is processed
    Then the trainer summary for "john.doe" should exist in the database

  Scenario: Get the training summary info for an existent user
    Given a request for a summary info for the user "john.doe"
    When the request is processed
    Then the trainer summary info for "john.doe" should be shown