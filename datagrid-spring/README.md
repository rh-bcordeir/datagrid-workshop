# datagrid-workshop-spring

This project is a Spring Boot version of the DataGrid Workshop application.

## Prerequisites

- Java 17
- Maven 3.6+
- Docker and Docker Compose (for PostgreSQL)

## Running the application

### 1. Start PostgreSQL

```shell script
docker-compose up -d postgres
```

### 2. Run the application

```shell script
./mvnw spring-boot:run
```

Or if you prefer to build and run:

```shell script
./mvnw clean package
java -jar target/datagrid-workshop-spring-1.0.0.jar
```

## API Endpoints

- `GET /movies` - List all movies (cached in DataGrid)
- `GET /movies/{id}` - Get a movie by ID (cached in DataGrid)
- `POST /movies` - Create a new movie
- `DELETE /movies/{id}` - Delete a movie by ID

## Configuration

The application configuration is in `src/main/resources/application.properties`.

### Database Configuration
- Default: PostgreSQL running on `postgres-db.application.svc.cluster.local:5432`
- Database: `cinema`
- Username: `postgres`
- Password: `postgres`

### Infinispan/DataGrid Configuration
- Hosts: `infinispan.data-grid.svc.cluster.local:11222`
- Username: `developer`
- Password: `N9FraDcmAEJCnBFx`

## Technologies

- Spring Boot 3.2.0
- Spring Data JPA
- PostgreSQL
- Infinispan Client (DataGrid)
- Protobuf for serialization

