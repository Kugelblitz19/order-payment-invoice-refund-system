# Order Payment Invoice Backend

A production-style backend system built using **Spring Boot** that manages the complete lifecycle of an order:
**Order → Payment → Invoice → Refund**.

This project demonstrates real-world backend design with proper layering, transactional consistency, callbacks handling, and testing.

---

## 🚀 Features

- Create and manage orders
- Initiate payment and handle payment callbacks
- Automatically generate invoices after successful payment
- Initiate refunds and handle refund callbacks
- Global exception handling
- Unit and integration testing
- Swagger API documentation

---

## 🛠 Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Hibernate**
- **MySQL**
- **Swagger (SpringDoc OpenAPI)**
- **JUnit 5**
- **Mockito**
- **MockMvc (Integration Testing)**

---

## 🧩 Architecture Overview

The project follows a **layered architecture**:

Controller → Service → Repository → Database


### Key Design Principles
- DTO-based request/response handling
- Clear separation of concerns
- Transactional boundaries at service layer
- Idempotent handling of callbacks
- Centralized global exception handling

---

## 🔄 Business Flow

1.Create Order

2.Initiate Payment

3.Payment Callback (SUCCESS / FAILED)

4.Invoice Generated (on SUCCESS)

5.Initiate Refund

6.Refund Callback (SUCCESS / FAILED)


---

## 📂 Project Structure

src
├── main
│ ├── controller
│ ├── service
│ ├── repository
│ ├── entity
│ ├── dtos
│ ├── enums
│ └── exception
└── test
├── service (Unit Tests)
└── integration (End-to-End Integration Test)


---

## 🧪 Testing Strategy

### Unit Tests
- OrderService
- PaymentService
- InvoiceService
- RefundService

### Integration Test
- Complete end-to-end flow tested using **MockMvc**
- Covers order creation → payment → invoice → refund lifecycle

---

## 📑 API Documentation

Swagger UI is available at:
http://localhost:8080/swagger-ui.html


---

## ▶️ How to Run the Project

### Prerequisites
- Java 17
- Maven
- MySQL running locally

### Steps

```bash
mvn clean install
mvn spring-boot:run

🧠 Key Learnings

Designing real-world payment and refund flows

Handling external callbacks safely

Writing clean and maintainable service-layer logic

Writing meaningful unit and integration tests

Structuring a backend project for scalability

👤 Author

Kunal Priyadarshi

Java Backend Developer | Java | Spring Boot







