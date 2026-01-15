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

### Remote Connection

To remotely connect to a datagrid cluster expose through a passthrough Route add the following properties:

```
# Infinispan Configuration
infinispan.remote.server-list=${DATAGRID_HOST}:443
infinispan.remote.sni-host-name=${DATAGRID_HOST}
infinispan.client.hotrod.security.ssl.enabled=true

# Truststore (required for TLS)
infinispan.remote.trust-store-file-name=${PATH_TO_TRUSTSTORE_P12}
infinispan.remote.trust-store-password=changeit
infinispan.remote.trust-store-type=PKCS12
```

And change the @Bean to this:
``` 
return (ConfigurationBuilder builder) -> builder
                .security().ssl().hostnameValidation(false)
                .clientIntelligence(ClientIntelligence.BASIC)
                .addContextInitializer(schema);
```

## Technologies

- Spring Boot 3.2.0
- Spring Data JPA
- PostgreSQL
- Infinispan Client (DataGrid)
- Protobuf for serialization

