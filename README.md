# Regional Customer Management Dashboard

[![Java Version](https://img.shields.io/badge/Java-21%2B-blue)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4%2B-green)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-19.0.0-blue)](https://reactjs.org/)
[![Vite](https://img.shields.io/badge/Vite-6.1.0-purple)](https://vitejs.dev/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17%2B-blue)](https://www.postgresql.org/)

A hierarchical region management system for banking operations, supporting country-province-city relationships and manager-customer assignments with regional scope validation.

## 📋 Table of Contents
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Installation](#-installation)
- [API Documentation](#-api-documentation)
- [Frontend Development](#-frontend-development)
- [Testing](#-testing)

## ✨ Features
- Hierarchical region management (Country → Province → City)
- Manager-customer assignment with regional scope validation
- Interactive dashboard with Google Charts visualization
- Real-time data synchronization
- Customer distribution analysis
- Region-based statistics

## 🔧 Tech Stack

### Backend
- Java 21
- Spring Boot 3.4+
- Spring Data JPA
- PostgreSQL 17+
- Lombok
- Gradle

### Frontend
- React 19.0.0
- Vite 6.1.0
- TypeScript
- Tailwind CSS v4
- Google Charts
- Axios

### Testing
- JUnit 5
- Mockito
- Spring Boot Test
- React Testing Library

## 🚀 Installation

### Prerequisites
- Java 21 JDK
- PostgreSQL 17+
- Node.js (v18 or higher)
- npm or yarn
- Maven 3.8+

### Backend Setup

1. **Database Setup**
```sql
-- Refer to postgresql_schema.sql
```

2. **Clone Repository**
```bash
git clone https://github.com/yourusername/customer-dashboard.git
cd customer-dashboard
```

3. **Configure Backend**
Create `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/db_name
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.format_sql=true
```

4. **Run Backend**
```bash
mvn spring-boot:run
```

### Frontend Setup

1. **Navigate to Frontend Directory**
```bash
cd frontend
```

2. **Install Dependencies**
```bash
npm install
```

3. **Configure Environment**
Create `.env` file:
```env
VITE_API_BASE_URL=http://localhost:9191/api
```

4. **Run Frontend**
```bash
npm run dev
```

## 📚 API Documentation

### Endpoints Overview

| Endpoint | Method | Description |
|----------|---------|-------------|
| `/api/managers` | GET | List all managers |
| `/api/managers/{id}/details` | GET | Get detailed manager statistics |
| `/api/managers/{id}/customers` | GET | List customers managed by specific manager |
| `/api/managers/{id}/hierarchy` | GET | Get complete region hierarchy for manager |
| `/api/managers/{id}/stats` | GET | Get customer distribution statistics |

### Example Responses

1. **Manager Details**
```json
{
  "managerId": 1,
  "managerName": "Manager A",
  "regionLevel": "CITY",
  "regionName": "Surabaya",
  "numberOfCustomers": 3,
  "numberOfCities": 1
}
```

2. **Regional Hierarchy**
```json
{
  "managerId": 1,
  "managerName": "Manager A",
  "hierarchy": {
    "country": "Indonesia",
    "province": "East Java",
    "city": "Surabaya"
  }
}
```

## 🎨 Frontend Development

### Project Structure
```
src/
├── components/
│   ├── ManagerSelect/
│   ├── Dashboard/
│   └── Charts/
├── services/
│   └── api/
├── types/
├── utils/
├── App.tsx
└── main.tsx
```

### Building for Production
```bash
npm run build
```

## 🧪 Testing

### Backend Testing
```bash
mvn test
```

### Frontend Testing
```bash
npm run test
```

Test coverage includes:
- Controller endpoint validation
- Service layer business logic
- Repository query correctness
- React component rendering
- User interaction flows
- API integration

## 🗄️ Database Schema

### Key Tables
- `regions`: Stores hierarchical location data
- `managers`: Manages regional supervisors
- `customers`: Tracks bank customers with regional assignments

### Relationships
- Regions have self-referential parent-child relationships
- Managers are linked to specific regions
- Customers are linked to both managers and regions

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some feature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Requirements
- Include test cases
- Maintain code style
- Update documentation
- Ensure all tests pass