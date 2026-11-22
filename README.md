# Smart Logistics & Fleet Management System

A backend REST API built with **Java 21, Spring Boot 4.2.0, Spring Data JPA, Hibernate and PostgreSQL** to manage customers, drivers, vehicles, shipments, bookings, shipment tracking and invoice generation.

The application follows a layered architecture and exposes REST endpoints for managing logistics operations and enforcing business rules.

## Features

- **Customer Management:** Create, retrieve, update and delete customer records.
- **Driver Management:** Maintain driver details and availability status.
- **Vehicle Management:** Manage vehicle registration, type, capacity and availability.
- **Shipment Management:** Create and manage shipments associated with customers.
- **Booking Management:** Assign available drivers and vehicles to shipments with validation checks.
- **Shipment Tracking:** Record shipment events and enforce valid status transitions.
- **Invoice Management:** Generate invoices with an 18% tax calculation and prevent duplicate invoices for a shipment.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot 4.2.0 | Backend application framework |
| Spring Web MVC | REST API development |
| Spring Data JPA | Database persistence |
| Hibernate | ORM |
| PostgreSQL | Relational database |
| Maven | Build and dependency management |
| IntelliJ IDEA | Development environment |
| Postman | API testing |

## Architecture

The application follows a layered architecture:

```text
Client / Postman
       |
       v
   Controller Layer
       |
       v
    Service Layer
       |
       v
  Repository Layer
       |
       v
    PostgreSQL
```

### Project Structure

```text
fleetmanagement/
│
├── src/
│   ├── main/
│   │   ├── java/com/logistics/fleetmanagement/
│   │   │   ├── controller/
│   │   │   │   ├── BookingController.java
│   │   │   │   ├── CustomerController.java
│   │   │   │   ├── DriverController.java
│   │   │   │   ├── InvoiceController.java
│   │   │   │   ├── ShipmentController.java
│   │   │   │   ├── TrackingEventController.java
│   │   │   │   └── VehicleController.java
│   │   │   │
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── FleetmanagementApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│
├── .gitignore
├── pom.xml
├── mvnw
└── mvnw.cmd
```

### Layer Responsibilities

- **Controller:** Exposes REST endpoints, accepts HTTP requests and returns responses.
- **Service:** Contains business logic and validation rules.
- **Repository:** Uses Spring Data JPA to interact with the database.
- **Entity:** Represents database tables and their relationships.

## Database Design

The application uses PostgreSQL with the database name:

```sql
fleet_management
```

### Main Entities

| Entity | Description |
|---|---|
| Customer | Stores customer information |
| Driver | Stores driver details and availability |
| Vehicle | Stores vehicle registration and capacity |
| Shipment | Stores shipment details and current status |
| Booking | Associates a shipment with a driver and vehicle |
| TrackingEvent | Stores shipment tracking history |
| Invoice | Stores invoice amount, tax and total |

### Entity Relationships

- A customer can have multiple shipments.
- Each shipment is associated with a customer.
- A booking references one shipment, one driver and one vehicle.
- A shipment can have multiple tracking events.
- Each shipment can have at most one invoice.

## Installation and Setup

### 1. Prerequisites

Install the following:

- Java Development Kit (JDK) 21
- PostgreSQL
- Git
- Postman (optional, for API testing)

### 2. Clone the Repository

```bash
git clone https://github.com/explorer2005/fleetmanagement.git
cd fleetmanagement
```

### 3. Create the PostgreSQL Database

Open PostgreSQL or pgAdmin and execute:

```sql
CREATE DATABASE fleet_management;
```

The application uses the PostgreSQL user `postgres` by default.

### 4. Configure Database Credentials

Open:

```text
src/main/resources/application.properties
```

The database configuration is:

```properties
spring.application.name=fleetmanagement

spring.datasource.url=jdbc:postgresql://localhost:5432/fleet_management
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8080
```

Set your PostgreSQL password as an environment variable.

**Windows PowerShell:**

```powershell
$env:DB_PASSWORD = "your_postgresql_password"
```

If running through IntelliJ IDEA, configure `DB_PASSWORD` under the Run Configuration's Environment variables.

Do not commit actual database passwords or other credentials to GitHub.

### 5. Run the Application

Using the Maven wrapper on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Alternatively, open the project in IntelliJ IDEA and run:

```text
FleetmanagementApplication.java
```

The application starts at:

```text
http://localhost:8080
```

Hibernate uses `ddl-auto=update` to update the database schema based on the entity mappings.

## REST API Documentation

**Base URL**

```text
http://localhost:8080
```

All request bodies shown below use JSON. For POST and PUT requests, set:

```http
Content-Type: application/json
```

### 1. Customer Management

Base endpoint:

```text
/api/customers
```

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/customers` | Create customer |
| GET | `/api/customers` | Retrieve all customers |
| GET | `/api/customers/{id}` | Retrieve customer by ID |
| PUT | `/api/customers/{id}` | Update customer |
| DELETE | `/api/customers/{id}` | Delete customer |

**Create Customer**

```http
POST /api/customers
```

Request:

```json
{
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "phone": "9876543210",
  "address": "Bangalore, Karnataka"
}
```

**Update Customer**

```http
PUT /api/customers/1
```

Request:

```json
{
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "phone": "9876543210",
  "address": "Mysore, Karnataka"
}
```

**Get All Customers**

```http
GET /api/customers
```

**Get Customer by ID**

```http
GET /api/customers/1
```

**Delete Customer**

```http
DELETE /api/customers/1
```

Business rule:
- Duplicate customer email registration is checked during customer creation.

### 2. Driver Management

Base endpoint:

```text
/api/drivers
```

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/drivers` | Create driver |
| GET | `/api/drivers` | Retrieve all drivers |
| GET | `/api/drivers/{id}` | Retrieve driver by ID |
| PUT | `/api/drivers/{id}` | Update driver |
| DELETE | `/api/drivers/{id}` | Delete driver |

**Create Driver**

```http
POST /api/drivers
```

Request:

```json
{
  "name": "Amit Kumar",
  "phone": "9876543211",
  "licenseNumber": "KA01202600123"
}
```

The default driver status is `AVAILABLE`.

**Update Driver**

```http
PUT /api/drivers/1
```

Request:

```json
{
  "name": "Amit Kumar",
  "phone": "9876543211",
  "licenseNumber": "KA01202600123",
  "status": "AVAILABLE"
}
```

**Get All Drivers**

```http
GET /api/drivers
```

**Get Driver by ID**

```http
GET /api/drivers/1
```

**Delete Driver**

```http
DELETE /api/drivers/1
```

Driver statuses used by the booking workflow:

- `AVAILABLE`
- `ASSIGNED`

### 3. Vehicle Management

Base endpoint:

```text
/api/vehicles
```

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/vehicles` | Create vehicle |
| GET | `/api/vehicles` | Retrieve all vehicles |
| GET | `/api/vehicles/{id}` | Retrieve vehicle by ID |
| PUT | `/api/vehicles/{id}` | Update vehicle |
| DELETE | `/api/vehicles/{id}` | Delete vehicle |

**Create Vehicle**

```http
POST /api/vehicles
```

Request:

```json
{
  "registrationNumber": "KA01AB1234",
  "vehicleType": "TRUCK",
  "capacity": 10.5
}
```

The default vehicle status is `AVAILABLE`.

**Update Vehicle**

```http
PUT /api/vehicles/1
```

Request:

```json
{
  "registrationNumber": "KA01AB1234",
  "vehicleType": "TRUCK",
  "capacity": 15.0,
  "status": "AVAILABLE"
}
```

**Get All Vehicles**

```http
GET /api/vehicles
```

**Get Vehicle by ID**

```http
GET /api/vehicles/1
```

**Delete Vehicle**

```http
DELETE /api/vehicles/1
```

Vehicle statuses used by the booking workflow:

- `AVAILABLE`
- `ASSIGNED`

### 4. Shipment Management

Base endpoint:

```text
/api/shipments
```

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/shipments` | Create shipment |
| GET | `/api/shipments` | Retrieve all shipments |
| GET | `/api/shipments/{id}` | Retrieve shipment by ID |
| GET | `/api/shipments/customer/{customerId}` | Retrieve shipments by customer |
| PUT | `/api/shipments/{id}` | Update shipment |
| DELETE | `/api/shipments/{id}` | Delete shipment |

**Create Shipment**

```http
POST /api/shipments
```

Request:

```json
{
  "description": "Electronic Goods",
  "weight": 5.5,
  "pickupLocation": "Bangalore",
  "deliveryLocation": "Chennai",
  "customer": {
    "id": 1
  }
}
```

The customer ID must refer to an existing customer.

The default shipment status is `CREATED`.

**Update Shipment**

```http
PUT /api/shipments/1
```

Request:

```json
{
  "description": "Electronic Goods",
  "weight": 5.5,
  "pickupLocation": "Bangalore",
  "deliveryLocation": "Chennai",
  "status": "CREATED"
}
```

The update operation changes the shipment description, weight, pickup location, delivery location and status. It does not change the associated customer.

**Get All Shipments**

```http
GET /api/shipments
```

**Get Shipment by ID**

```http
GET /api/shipments/1
```

**Get Shipments by Customer**

```http
GET /api/shipments/customer/1
```

**Delete Shipment**

```http
DELETE /api/shipments/1
```

Shipment statuses used in the application include:

- `CREATED`
- `BOOKED`
- `PICKED_UP`
- `IN_TRANSIT`
- `DELIVERED`
- `CANCELLED`

### 5. Booking Management

Base endpoint:

```text
/api/bookings
```

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/bookings` | Create booking |
| GET | `/api/bookings` | Retrieve all bookings |
| GET | `/api/bookings/{id}` | Retrieve booking by ID |
| PUT | `/api/bookings/{id}/cancel` | Cancel booking |

**Create Booking**

```http
POST /api/bookings
```

Request:

```json
{
  "shipment": {
    "id": 1
  },
  "driver": {
    "id": 1
  },
  "vehicle": {
    "id": 1
  }
}
```

The application validates the shipment, driver and vehicle before creating a booking.

**Booking Validation Rules**

1. Shipment, driver and vehicle IDs are required.
2. The shipment must exist.
3. The driver must exist.
4. The vehicle must exist.
5. Delivered or cancelled shipments cannot be booked.
6. A shipment cannot have another active `CONFIRMED` booking.
7. The driver must have `AVAILABLE` status.
8. The vehicle must have `AVAILABLE` status.
9. Vehicle capacity must be greater than or equal to shipment weight.

**Successful Booking Workflow**

When a booking is created:

```text
Booking Status  = CONFIRMED
Driver Status   = ASSIGNED
Vehicle Status  = ASSIGNED
Shipment Status = BOOKED
```

**Get All Bookings**

```http
GET /api/bookings
```

**Get Booking by ID**

```http
GET /api/bookings/1
```

**Cancel Booking**

```http
PUT /api/bookings/1/cancel
```

When a confirmed booking is cancelled:

```text
Booking Status  = CANCELLED
Driver Status   = AVAILABLE
Vehicle Status  = AVAILABLE
Shipment Status = CREATED
```

Only bookings with `CONFIRMED` status can be cancelled.

### 6. Shipment Tracking

Base endpoint:

```text
/api/tracking
```

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/tracking` | Add tracking event |
| GET | `/api/tracking/shipment/{shipmentId}` | Retrieve shipment tracking history |
| DELETE | `/api/tracking/{id}` | Delete tracking event |

**Add Tracking Event**

```http
POST /api/tracking
```

Request:

```json
{
  "shipment": {
    "id": 1
  },
  "location": "Bangalore",
  "status": "PICKED_UP",
  "description": "Shipment picked up from warehouse"
}
```

**Shipment Status Transition Rules**

The application enforces the following sequence:

```text
CREATED
   |
   v
PICKED_UP
   |
   v
IN_TRANSIT
   |
   v
DELIVERED
```

Only the following transitions are accepted:

| Current Status | New Status |
|---|---|
| CREATED | PICKED_UP |
| PICKED_UP | IN_TRANSIT |
| IN_TRANSIT | DELIVERED |

Invalid status transitions are rejected.

Adding a tracking event also updates the associated shipment's current status. The operation is transactional.

**Get Tracking History**

```http
GET /api/tracking/shipment/1
```

Tracking events are retrieved in ascending event-time order.

**Delete Tracking Event**

```http
DELETE /api/tracking/1
```

### 7. Invoice Management

Base endpoint:

```text
/api/invoices
```

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/invoices/shipment/{shipmentId}?amount={amount}` | Generate invoice |
| GET | `/api/invoices` | Retrieve all invoices |
| GET | `/api/invoices/{id}` | Retrieve invoice by ID |
| GET | `/api/invoices/shipment/{shipmentId}` | Retrieve invoice by shipment |

**Generate Invoice**

```http
POST /api/invoices/shipment/1?amount=10000
```

The invoice amount is provided as a query parameter, not a JSON request body.

**Invoice Calculation**

The current implementation uses a fixed tax rate of 18%.

```text
Amount      = 10000
Tax Rate    = 18%
Tax         = 1800
Total       = 11800
```

Formula:

```text
Tax         = Amount × 0.18
Total Amount = Amount + Tax
```

**Invoice Validation Rules**

- The shipment must exist.
- Cancelled shipments cannot be invoiced.
- Invoice amount must be greater than zero.
- Only one invoice can be generated for a shipment.

The one-invoice-per-shipment rule is checked in the service layer and reinforced by a unique database constraint on `shipment_id`.

**Get All Invoices**

```http
GET /api/invoices
```

**Get Invoice by ID**

```http
GET /api/invoices/1
```

**Get Invoice by Shipment ID**

```http
GET /api/invoices/shipment/1
```

The invoice status defaults to `GENERATED`.

The invoice module generates invoice records only. It does not implement payment processing.

## Business Logic Summary

The main business workflows are:

### Shipment Booking

```text
Select Shipment
      |
      v
Validate Shipment
      |
      v
Validate Driver
      |
      v
Validate Vehicle
      |
      v
Check Availability
      |
      v
Check Vehicle Capacity
      |
      v
Create Booking
      |
      v
Update Driver, Vehicle
and Shipment Status
```

### Shipment Tracking

```text
CREATED
   |
   | Pickup event
   v
PICKED_UP
   |
   | Transit event
   v
IN_TRANSIT
   |
   | Delivery event
   v
DELIVERED
```

### Invoice Generation

```text
Receive Shipment ID and Amount
             |
             v
      Validate Shipment
             |
             v
   Check Shipment Cancellation
             |
             v
       Validate Amount
             |
             v
   Check Existing Invoice
             |
             v
     Calculate 18% Tax
             |
             v
       Save Invoice
```

## Exception Handling and Validation

The current implementation performs business validation inside service classes.

Examples of business exceptions include:

| Scenario | Exception Message |
|---|---|
| Customer not found | `Customer not found` |
| Duplicate customer email | `Email already registered` |
| Driver not found | `Driver not found` |
| Vehicle not found | `Vehicle not found` |
| Shipment not found | `Shipment not found` |
| Booking not found | `Booking not found` |
| Missing booking IDs | `Shipment, driver and vehicle IDs are required` |
| Shipment already booked | `Shipment already has an active booking` |
| Driver unavailable | `Driver is not available` |
| Vehicle unavailable | `Vehicle is not available` |
| Insufficient capacity | `Vehicle capacity is insufficient` |
| Invalid tracking transition | `Invalid shipment status transition` |
| Duplicate invoice | `Invoice already exists for this shipment` |
| Invalid invoice amount | `Invoice amount must be greater than zero` |

### Current Exception Handling Behavior

- Business validation failures are thrown as `RuntimeException`.
- The application does not currently implement a custom `@RestControllerAdvice` or `@ExceptionHandler`.
- Under the default Spring MVC error handling, uncaught runtime exceptions generally result in HTTP 500 responses.
- The precise error response body depends on the Spring Boot error configuration.
- Database constraint violations may also produce persistence-related errors.

A centralized exception handler and more specific HTTP error responses can be added as future improvements.

## Testing

The REST APIs can be tested using Postman.

For each request:

1. Start the Spring Boot application.
2. Open Postman.
3. Use `http://localhost:8080` as the base URL.
4. Select the required HTTP method.
5. Add the endpoint path.
6. For POST and PUT requests, select **Body → raw → JSON**.
7. Send the request and inspect the response.

For a complete workflow, create a customer, driver and vehicle first, then create a shipment, book it, add tracking events and generate its invoice.

Use IDs returned by your own API responses when testing relationships.

## Future Improvements

Potential enhancements include:

- Centralized exception handling using `@RestControllerAdvice`.
- Bean Validation using annotations such as `@NotBlank`, `@NotNull` and `@Positive`.
- DTOs for request and response separation.
- Pagination and sorting.
- Authentication and role-based authorization.
- Automated integration and unit tests.
- API documentation using Swagger / OpenAPI.
- Payment processing integration.

These are potential future enhancements and are not represented as currently implemented features.

## Repository

GitHub:

https://github.com/explorer2005/fleetmanagement

## Author

Developed as a Smart Logistics & Fleet Management backend project using Java, Spring Boot and PostgreSQL.
