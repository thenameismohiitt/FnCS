# Warehouse Colocation Management System

A Java/Quarkus-based based **Warehouse Colocation Management System**. The project combines a hands-on coding challenge with a business case-study discussion.

---

## 📋 Table of Contents

- [Domain Overview](#-domain-overview)
- [Project Structure](#-project-structure)
- [Tech Stack](#-tech-stack)

---

## 🏭 Domain Overview

The system manages **warehouse colocation** — tracking where products are stored and how warehouses serve stores across different geographic locations.

### Core Entities

| Entity | Description |
|---|---|
| `Location` | A geographical place (city) |
| `Warehouse` | A distribution hub where products are stored before being sent to stores |
| `Store` | A physical retail location where products are sold to customers |
| `Product` | Goods sold at stores, fulfilled from warehouses |

### Key Business Rules

- Each `Location` has a **maximum number of warehouses** it can accommodate.
- Each `Location` has a **maximum storage capacity** shared across its warehouses.
- A `Warehouse` can be **replaced**: the old one is archived and the new one inherits the same **Business Unit Code**, preserving operational history.
- A `Warehouse` can serve as a **fulfilment unit** for specific products at specific stores, subject to fulfilment constraints.

---

## 📁 Project Structure

```
fcs-interview-code-assignment-main/
├── java-assignment/          
│   ├── src/
│   │   ├── main/java/com/fulfilment/application/monolith/
│   │   │   ├── location/     
│   │   │   ├── stores/        
│   │   │   ├── warehouses/    
│   │   │   │   ├── adapters/ 
│   │   │   │   └── domain/    
│   │   │   ├── fulfillment/   
│   │   │   └── products/      
│   │   └── test/              
│   ├── CODE_ASSIGNMENT.md    
│   └── pom.xml
├── case-study/
│   ├── BRIEFING.md           
│   ├── CASE_STUDY.md          
│   └── CODE_ASSIGNMENT.md    
├── .github/workflows/ci.yml
├── CONTRIBUTING.md
└── README.md
```

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | [Quarkus 3.13](https://quarkus.io/) |
| Persistence | Hibernate ORM with Panache + PostgreSQL |
| REST | Quarkus REST (JAX-RS / Jackson) |
| Testing | JUnit 5, REST-Assured, Quarkus Test |
| Coverage | JaCoCo |
| Build | Maven Wrapper (`./mvnw`) |
| CI | GitHub Actions |

---
