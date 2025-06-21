Feature: Trainer Workload Queue Integration

  Scenario: Trainer workload message is processed and saved in MongoDB
    Given a valid workload message with username "johnny.test"
    When the message is sent to the trainer workload queue
    Then the trainer "johnny.test" should be stored in MongoDB
