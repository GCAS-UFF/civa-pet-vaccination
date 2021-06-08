# Vaccination MS

Microservice responsible for handling the core domain: vaccination context.  
The vaccination card is the root aggregate of the microservice

## Architecture
The main idea is to build a reactive microservice.

Patterns employed:
- Domain Driven Design (DDD)
- Behavior Driven Development (BDD)
- Hexagonal Architecture
- CI/CD
- Reactive Programming with Kotlin coroutines

Ideas to explore:
- Kafka
- CQRS
- Server-sent Events
- Kubernetes
- AWS

## Observations
This project is using GitHub Action checks for testing the application on commits,  
building on pull requests and scanning on both.

## Dependencies
- Kotlin
- Spring WebFlux
- Spring Actuator
- Spring Cloud Stream
