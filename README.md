# Teleport Tracking System

A robust, reactive tracking number generation and management system built with Spring Boot WebFlux, MongoDB, and Java 21.

## Project Overview

The Teleport Tracking System is designed to generate unique tracking numbers for shipments and manage tracking information throughout the logistics process. The system implements a reactive programming model using Spring WebFlux to handle high throughput with minimal resource consumption.

### Key Features

- **Reactive API**: Built with Spring WebFlux for non-blocking operations
- **Unique Tracking Number Generation**: Automatically generates unique tracking numbers with retry mechanism
- **Data Validation**: Comprehensive input validation for all API requests
- **Swagger Documentation**: Interactive API documentation with OpenAPI 3
- **Modular Architecture**: Clean separation of concerns with a multi-module Maven project

### Technology Stack

- **Java 21**
- **Spring Boot 3.2.3**
- **Spring WebFlux** for reactive programming
- **MongoDB** for data storage
- **Maven** for dependency management and building
- **Lombok** for reducing boilerplate code
- **SpringDoc OpenAPI** for API documentation

## API Documentation

The Tracking System exposes RESTful endpoints for tracking number generation and management.

### Swagger UI

You can access the interactive API documentation at:

```
http://localhost:8081/swagger-ui.html
```

The OpenAPI specification is available at:

```
http://localhost:8081/api-docs
```

### Tracking Number Controller API

The `TrackingNumberController` provides endpoints for tracking number generation and management:

#### Generate Next Tracking Number

```
GET /api/v1/tracking-number/next
```

Generates a unique tracking number and creates a new tracking record in the database.

**Request Parameters:**

| Parameter | Type | Description | Format/Validation |
|-----------|------|-------------|-------------------|
| origin_country_id | String | Origin country code | ISO 3166-1 alpha-2 format (e.g., SG, ID) |
| destination_country_id | String | Destination country code | ISO 3166-1 alpha-2 format (e.g., SG, ID) |
| weight | Double | Package weight | Greater than 0.001 |
| created_at | String | Order creation timestamp | ISO-8601 date-time format with offset |
| customer_id | String | Customer UUID | UUID format |
| customer_name | String | Customer name | Alphanumeric, max 100 chars |
| customer_slug | String | Customer identifier | Kebab-case format |

**Response:**

```json
{
  "code": "SUCCESS",
  "message": "Success",
  "data": {
    "tracking_number": "TLP1234567890",
    "created_at": "2025-01-01T00:00:00+08:00"
  },
  "server_time": "2025-01-01T00:00:00+08:00"
}
```

## Deployed Application

A live instance of this application is currently deployed and available for testing:

### API Endpoints

- **Base URL**: http://4.145.124.75:8081
- **Swagger UI**: http://4.145.124.75:8081/swagger-ui.html
- **OpenAPI Docs**: http://4.145.124.75:8081/api-docs

You can use the Swagger UI to explore and test all available API endpoints directly from your browser.

### Sample Request

To generate a tracking number using the deployed application, you can make a GET request to:

```
http://4.145.124.75:8081/api/v1/tracking-number/next?origin_country_id=MY&destination_country_id=ID&weight=1.234&created_at=2018-11-20T19%3A29%3A32%2B08%3A00&customer_id=de619854-b59b-425e-9db4-943979e1bd49&customer_name=RedBox%20Logistics&customer_slug=redbox-logistics
```

## Database Documentation

The application uses MongoDB for data storage. The following collections are used:

### Collections

#### 1. Tracking Collection

Stores tracking information for shipments.

**Key Fields:**
- `trackingNumber` (String): Unique identifier for the shipment (indexed, unique)
- `originCountryId` (String): ISO 3166-1 alpha-2 country code for origin
- `destinationCountryId` (String): ISO 3166-1 alpha-2 country code for destination
- `weight` (Double): Package weight
- `customerId` (String): UUID of the customer
- `customerName` (String): Name of the customer
- `customerSlug` (String): Kebab-case identifier for the customer
- `orderCreatedAt` (Instant): When the order was created
- `status` (Enum): Current status of the tracking (CREATED, IN_TRANSIT, DELIVERED, etc.)
- Standard audit fields inherited from BaseMongo (createdAt, updatedAt, etc.)

#### 2. DbSequence Collection

Manages sequence generation for tracking numbers.

**Key Fields:**
- `key` (String): Identifier for the sequence type (indexed, unique)
- `sequence` (Long): Current sequence value

### Indexes

The following indexes are configured in MongoDB:

**Tracking Collection:**
- `trackingNumber` (unique): Ensures uniqueness of tracking numbers

**DbSequence Collection:**
- `key` (unique): Ensures uniqueness of sequence keys

## Building and Running the Project

### Prerequisites

- Java Development Kit (JDK) 21
- Maven 3.8+
- MongoDB 6.0+

### Local Development

#### 1. Clone the repository

```bash
git clone https://github.com/jaysyanshar/tracking-system.git
cd teleport-tracking-system
```

#### 2. Configure MongoDB

Make sure MongoDB is running locally on the default port (27017) or update the connection details in `application.properties`.

#### 3. Build the project

```bash
mvn clean install
```

#### 4. Run the application

```bash
cd rest-api
mvn spring-boot:run
```

The application will start on port 8081 by default.

### Deployment with Docker

This project includes Docker configuration for easy deployment with Docker and Docker Compose.

#### Prerequisites

- Docker
- Docker Compose

#### Quick Start

1. Clone the repository

```bash
git clone https://github.com/jaysyanshar/tracking-system.git
cd tracking-system
```

2. Start the application with Docker Compose

```bash
docker-compose up -d
```

This command will:
- Build the application Docker image
- Start a MongoDB container
- Start the application container
- Connect the application to MongoDB
- Expose the application on port 8081

3. Access the application

```
http://localhost:8081
```

The Swagger UI is available at:

```
http://localhost:8081/swagger-ui.html
```

#### Container Details

The Docker Compose setup includes:

- **Application Container**: Runs the Spring Boot application
- **MongoDB Container**: Provides the database
- **Persistent Volume**: Stores MongoDB data across container restarts
- **Docker Network**: Enables communication between containers

#### Environment Configuration

You can modify environment variables in the `docker-compose.yml` file:

```yaml
environment:
  - SPRING_DATA_MONGODB_URI=mongodb://mongodb:27017/teleport_tracking
  - SERVER_TIMEZONE=+08:00
```

#### Stopping the Application

To stop the application:

```bash
docker-compose down
```

To stop the application and remove volumes:

```bash
docker-compose down -v
```

## Additional Information

### Sequence Generation Mechanism

The tracking number generation is handled by the `SequenceGeneratorService` which:

1. Uses MongoDB's atomic operations to generate sequential numbers
2. Prepends a prefix ("TLP") to the sequence number
3. Implements a retry mechanism to handle concurrent updates (optimistic locking)
4. Configurable retries (default: 3 attempts with 50ms delay between retries)

### Error Handling

The application uses a global exception handler to transform exceptions into standardized responses with appropriate HTTP status codes and error messages.

### Timezone Configuration

The application uses the timezone specified in the `server.timezone` property (default: +08:00) for formatting date-time values in responses.

## Maintenance and Support

For any issues or questions, please contact the developer at [jaysyanshar98@gmail.com](mailto:jaysyanshar98@gmail.com).
