# Steps to Run

## 1. Configure the Database

Update the following file:

`src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/product_catalog
spring.datasource.username=postgres
spring.datasource.password=postgres
```

## 2. Start the Application

Run the application using your preferred method 

## 3. Automatic Database Setup

On startup, Liquibase migrations run automatically and will:

- Create the required database schema
- Seed example product data


## 4. Application Details
- Default Port: 8080
- Swagger UI: http://localhost:8080/swagger-ui.html
