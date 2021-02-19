# Vaccination MS

Microservice responsible for handling the core domain: vaccination context.  
The vaccination card is the root aggregate of the microservice

## Architecture
The main idea is to build a reactive microservice.

Patterns employed:
- Domain Driven Design
- Ports and Adapaters Architecture
- Reactive Paradigm

Ideas to explore:
- Event Sourcing
- CQRS
- Cloud Native
- RSocket, Event Streaming or Server Side Events (SSE)

## Observations
This project is using GitHub Action checks for testing the application on commits and building on pull requests.

## Dependencies
- Kotlin
- Spring WebFlux
- Spring Actuator
- Spring Cloud Stream
