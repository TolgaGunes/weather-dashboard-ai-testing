# Weather Dashboard – AI-Augmented Testing

This project demonstrates an AI-augmented testing approach using a Spring Boot application integrated with the OpenWeather API.

## Key Features
- Spring Boot REST API consuming a real external service
- JUnit 5 API contract tests
- Controlled flaky test simulation
- Isolation of flaky tests using JUnit tags
- Structured failure logging for AI-based failure analysis

## Tech Stack
- Java 17
- Spring Boot
- Maven
- JUnit 5

## Goal
To showcase how AI can be used as a decision-support tool for test failure analysis in CI/CD pipelines.

# How to Run

### Normal test: 
    - mvn test

### Flaky sim: 
    - mvn test -DincludeTags=flaky-sim

### UTF-8 log generate: 
    PowerShell Out-File -Encoding utf8  

    (mvn -q test -DincludeTags=flaky-sim | Out-File -FilePath logs\flaky-run-1.log -Encoding utf8)

### Failure analysis: 
    FailureAnalysisRunner