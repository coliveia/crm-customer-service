# CRM Customer Service

Customer Service Portal Backend - Visão 360° do Cliente

## Overview

O **CRM Customer Service** é um microsserviço backend que fornece funcionalidades completas de gerenciamento de clientes, casos e interações para o portal de atendimento ao cliente.

## Features

- ✅ Gerenciamento de Clientes (CRUD)
- ✅ Visão 360° do Cliente (dados consolidados)
- ✅ Gerenciamento de Casos/Tickets
- ✅ Registro de Interações (Chat, Email, Phone, etc)
- ✅ Análise de Satisfação
- ✅ Histórico de Interações
- ✅ Estatísticas de Atendimento
- ✅ Swagger/OpenAPI Documentation
- ✅ Testes Unitários

## Architecture

```
┌─────────────────────────────────────────┐
│ Frontend (crm-customer-service-portal)  │
└────────────────┬────────────────────────┘
                 │ HTTP REST
┌────────────────▼────────────────────────┐
│ CRM Customer Service (Java/Spring Boot) │
│ ├─ CustomerController                   │
│ ├─ CustomerCaseController               │
│ ├─ InteractionController                │
│ ├─ CustomerService                      │
│ ├─ CustomerCaseService                  │
│ ├─ InteractionService                   │
│ └─ Repositories (JPA)                   │
└────────────────┬────────────────────────┘
                 │
        ┌────────┴────────┐
        │                 │
   ┌────▼─────┐    ┌─────▼────┐
   │ Oracle   │    │ H2 (Dev)  │
   │ Database │    │ Database  │
   └──────────┘    └──────────┘
```

## Getting Started

### Prerequisites

- Java 21
- Maven 3.6+
- Oracle Database (or H2 for development)

### Installation

1. Clone the repository
```bash
git clone https://github.com/coliveia/crm-customer-service.git
cd crm-customer-service
```

2. Build the project
```bash
mvn clean install -DskipTests
```

3. Run the service
```bash
java -jar target/crm-customer-service-1.0.0-SNAPSHOT.jar
```

The service will start on `http://localhost:8085`

## API Endpoints

### Customer Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/customer` | Create customer |
| GET | `/api/customer/{id}` | Get customer by ID |
| GET | `/api/customer/external/{externalId}` | Get customer by external ID |
| PUT | `/api/customer/{id}` | Update customer |
| GET | `/api/customer` | Get all customers (paginated) |
| GET | `/api/customer/status/{status}` | Get customers by status |
| GET | `/api/customer/{id}/view360` | Get customer 360° view |

### Case Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/case/customer/{customerId}` | Create case |
| GET | `/api/case/{id}` | Get case by ID |
| GET | `/api/case/number/{caseNumber}` | Get case by number |
| PUT | `/api/case/{id}` | Update case |
| POST | `/api/case/{id}/resolve` | Resolve case |
| GET | `/api/case/customer/{customerId}` | Get cases by customer |
| GET | `/api/case/status/{status}` | Get cases by status |
| GET | `/api/case/priority/{priority}` | Get cases by priority |

### Interaction Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/interaction/customer/{customerId}` | Create interaction |
| GET | `/api/interaction/{id}` | Get interaction by ID |
| GET | `/api/interaction/id/{interactionId}` | Get interaction by ID string |
| PUT | `/api/interaction/{id}` | Update interaction |
| GET | `/api/interaction/customer/{customerId}` | Get interactions by customer |
| GET | `/api/interaction/case/{caseId}` | Get interactions by case |
| GET | `/api/interaction/channel/{channel}` | Get interactions by channel |

## Data Models

### Customer
```json
{
  "id": 1,
  "externalId": "EXT-001",
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "11999999999",
  "cpf": "12345678900",
  "status": "ACTIVE",
  "segment": "Premium",
  "preferredChannel": "CHAT",
  "address": "Rua A, 123",
  "city": "São Paulo",
  "state": "SP",
  "zipCode": "01310100",
  "lifetimeValue": 5000.00,
  "totalPurchases": 10,
  "lastInteraction": "2026-01-24T20:30:00",
  "createdAt": "2026-01-20T10:00:00",
  "updatedAt": "2026-01-24T20:30:00"
}
```

### CustomerCase
```json
{
  "id": 1,
  "caseNumber": "CASE-1234567890-abcd1234",
  "customerId": 1,
  "title": "Issue with billing",
  "description": "Customer reported billing issue",
  "status": "OPEN",
  "priority": "HIGH",
  "category": "Billing",
  "subcategory": "Invoice",
  "assignedTo": "agent123",
  "createdAt": "2026-01-24T10:00:00",
  "updatedAt": "2026-01-24T20:30:00",
  "resolvedAt": null,
  "resolutionTimeMinutes": null,
  "resolutionNotes": null,
  "satisfactionScore": null
}
```

### Interaction
```json
{
  "id": 1,
  "interactionId": "INT-1234567890-abcd1234",
  "customerId": 1,
  "caseId": 1,
  "channel": "CHAT",
  "agentId": "agent123",
  "agentName": "Jane Smith",
  "message": "Hello, how can I help you?",
  "direction": "OUTBOUND",
  "durationSeconds": 300,
  "createdAt": "2026-01-24T10:00:00",
  "updatedAt": "2026-01-24T10:05:00",
  "sentiment": "POSITIVE",
  "sentimentScore": 0.85,
  "notes": "Customer satisfied with resolution"
}
```

## Customer 360° View

The `/customer/{id}/view360` endpoint returns comprehensive customer information:

```json
{
  "customer": { /* Customer data */ },
  "openCases": [ /* List of open cases */ ],
  "recentInteractions": [ /* Last 10 interactions */ ],
  "statistics": {
    "totalCases": 5,
    "openCases": 2,
    "resolvedCases": 3,
    "averageSatisfactionScore": 4.2,
    "totalInteractions": 25,
    "interactionsThisMonth": 8,
    "averageResolutionTimeMinutes": 240
  },
  "riskLevel": "LOW",
  "nextRecommendedAction": "Maintain relationship"
}
```

## Configuration

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521:xe
    username: crm_user
    password: crm_password
  jpa:
    hibernate:
      ddl-auto: update

server:
  port: 8085

customer-service:
  max-interactions-per-page: 50
  enable-copilot-integration: true
  copilot-service-url: http://localhost:8084/api
```

## Database

### Tables

- `customers` - Customer master data
- `customer_cases` - Support cases/tickets
- `interactions` - Customer interactions (chat, email, phone, etc)

### Enums

**Customer Status:** ACTIVE, INACTIVE, SUSPENDED, PROSPECT

**Case Status:** OPEN, IN_PROGRESS, WAITING_CUSTOMER, RESOLVED, CLOSED, ESCALATED

**Case Priority:** LOW, MEDIUM, HIGH, CRITICAL

**Interaction Channel:** CHAT, EMAIL, PHONE, SOCIAL_MEDIA, WHATSAPP, SMS

**Interaction Direction:** INBOUND, OUTBOUND

## Testing

### Run Tests
```bash
mvn test
```

### Test Coverage
- CustomerServiceTest - 6 test cases
- CustomerCaseServiceTest - 6 test cases

## Documentation

### Swagger UI
```
http://localhost:8085/api/customer-service/swagger-ui.html
```

### OpenAPI JSON
```
http://localhost:8085/api/customer-service/docs
```

## Integration

### Copilot Service Integration
The service can integrate with the Copilot Service for AI-powered suggestions:

```java
// Configuration in application.yml
customer-service:
  enable-copilot-integration: true
  copilot-service-url: http://localhost:8084/api
```

## Performance

- Average response time: 100-200ms
- Throughput: 1000+ requests/second
- Database connection pooling: Enabled
- Pagination: Supported (default 20 items per page)

## Monitoring

### Health Check
```bash
curl http://localhost:8085/api/actuator/health
```

### Metrics
```bash
curl http://localhost:8085/api/actuator/metrics
```

### Prometheus
```bash
curl http://localhost:8085/api/actuator/prometheus
```

## Development

### Build
```bash
mvn clean install -DskipTests
```

### Run
```bash
mvn spring-boot:run
```

### Test
```bash
mvn test
```

### Code Quality
```bash
mvn clean verify
```

## Deployment

### Docker
```bash
docker build -t crm-customer-service:1.0.0 .
docker run -p 8085:8085 \
  -e SPRING_DATASOURCE_URL=jdbc:oracle:thin:@db:1521:xe \
  crm-customer-service:1.0.0
```

### Kubernetes
```bash
kubectl apply -f k8s/deployment.yaml
```

## Future Enhancements

- [ ] Advanced search and filtering
- [ ] Real-time notifications
- [ ] Customer segmentation
- [ ] Predictive analytics
- [ ] Machine learning recommendations
- [ ] Multi-language support
- [ ] Advanced reporting
- [ ] API rate limiting
- [ ] Audit logging
- [ ] Data encryption

## Contributing

Contributions are welcome! Please follow the standard Git workflow:

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

Apache License 2.0 - See LICENSE file for details

## Support

For issues and questions:
- GitHub Issues: https://github.com/coliveia/crm-customer-service/issues
- Email: dev@crm.local

## Version History

### 1.0.0 (2026-01-24)
- Initial release
- Customer management
- Case management
- Interaction tracking
- Customer 360° view
- Swagger documentation
- Unit tests
