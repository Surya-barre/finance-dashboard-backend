# 📊 Finance Dashboard Backend

---

## 🧠 Project Overview

This project is a backend application built using **Spring Boot** that manages financial records and users with role-based access control.

The system allows users to:

* Track income and expenses
* View summarized financial insights
* Manage users with different roles (ADMIN, ANALYST, VIEWER)

The focus of this project is to demonstrate clean backend design, proper validation, and structured API development.

---

## ⚙️ Tech Stack

* Java (Spring Boot)
* Spring Data JPA
* MySQL Database
* Maven
* Swagger (OpenAPI for API documentation & testing)

---

## 🚀 Setup Instructions

## 🧩 Project Configuration

This project is built using the following versions and tools:

- **Spring Boot Version:** 3.5.13
- **Java Version:** 17
- **Build Tool:** Maven
- **OpenAPI (Swagger):** 2.8.16
- **Database:** MySQL

 
### 1. Clone Repository

```bash
git clone https://github.com/Surya-barre/finance-dashboard-backend.git
cd finance-dashboard-backend
```

### 2. Configure Database

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/finance_db
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3. Run Application

```bash
mvn spring-boot:run
```

### 4. Access APIs

* Swagger UI → `http://localhost:8080/swagger-ui.html`
* Used for testing and exploring APIs

---

## 📡 API Documentation

Swagger is integrated to:

* Test APIs
* Understand request/response
* Explore endpoints easily

---

## 👤 User & Role Management

### Roles:

* **ADMIN** → Full access (create, update, delete)
* **ANALYST** → View records + summaries
* **VIEWER** → Read-only access

---

### User APIs

| Method | Endpoint               | Description                   |
| ------ | ---------------------- | ----------------------------- |
| POST   | /users                 | Create user                   |
| GET    | /users                 | Get all users                 |
| GET    | /users/{id}            | Get single user               |
| PUT    | /users/{id}            | Full update                   |
| PATCH  | /users/{id}            | Partial update                |
| PATCH  | /users/{id}/deactivate | Deactivate user (soft delete) |

---

## 💰 Financial Records

### Record APIs

| Method | Endpoint                        | Description             |
| ------ | ------------------------------- | ----------------------- |
| POST   | /records?userId=1               | Create record (ADMIN)   |
| GET    | /records?userId=1&page=0&size=5 | Get records (paginated) |
| PUT    | /records/{id}?userId=1          | Update record           |
| PATCH  | /records/{id}?userId=1          | Partial update          |
| DELETE | /records/{id}?userId=1          | Delete record           |

---

## 🔍 Filtering & Pagination

Supports flexible filtering:

```
/records/filter?userId=1&type=INCOME
/records/filter?userId=1&type=INCOME&page=0&size=5
```

* Filter by type, category, date
* Pagination optional
* Improves performance for large datasets

---

## 📊 Summary APIs

| Endpoint                  | Description                    |
| ------------------------- | ------------------------------ |
| /records/summary          | Total income, expense, balance |
| /records/summary/category | Category-wise totals           |
| /records/summary/recent   | Recent transactions            |
| /records/summary/monthly  | Monthly trends                 |

---

## 🔐 Access Control

Role-based access implemented at controller level:

* ADMIN → Full CRUD access
* ANALYST → Read + summary
* VIEWER → Read-only

Inactive users are restricted from accessing APIs.

---

## ✅ Validation & Error Handling

### Validation:

* Name must not be blank and minimum length enforced
* Email must be valid format
* Role must not be null
* Amount must be positive
* Date must not be null

### Error Handling:

* CustomException used for business logic errors
* GlobalExceptionHandler handles all exceptions

### Status Codes:

* 200 → OK (Success)
* 201 → Created (Resource created successfully)
* 400 → Bad Request
* 403 → Forbidden
* 404 → Not Found

---

## 💾 Data Persistence

* MySQL database used
* JPA/Hibernate for ORM
* Data persists across application restarts

---

## 🧠 Assumptions Made

* Users are identified by unique email
* Role values must be uppercase (ADMIN, ANALYST, VIEWER)
* User must be active to access APIs
* Single-user context used for records (no ownership relation)
* Filtering parameters are optional

---

## ⚖️ Design Decisions & Trade-offs

* Used simple `userId`-based approach instead of complex relationships
* Implemented controller-level access control instead of Spring Security
* Avoided authentication (JWT) to keep focus on backend logic
* Prioritized readability, maintainability, and correctness

---

## 🚀 Future Improvements

* Add authentication (JWT / sessions)
* Implement User ↔ Record relationships
* Add search functionality
* Add unit & integration testing
* Add rate limiting
* Introduce DTO (Data Transfer Objects) to separate internal entity models from API responses and improve data security and flexibility

---

## 🎯 Key Highlights

* Clean REST API design
* Role-based access control
* Pagination & filtering support
* Structured exception handling
* Dashboard analytics implementation

---

## 💥 Conclusion

This project demonstrates a well-structured backend system with:

* User management
* Financial tracking
* Aggregated insights
* Validation and error handling

It focuses on clarity, correctness, and maintainability over unnecessary complexity.

---
