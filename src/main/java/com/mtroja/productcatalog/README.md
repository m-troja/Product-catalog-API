Steps to run:

1. Configure DB

### DB Config

src/main/application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/product_catalog
spring.datasource.username=postgres
spring.datasource.password=postgres

Default port: 8080

Then start application.
Liquibase migrations execute automatically.
Migration will seed example data.
Product attributes are stored in JSONB to support variable product specifications & attributes.
Swagger available at /swagger-ui.html (default: localhost:8080/swagger-ui.html)


 across manufacturers (from smartphones to vehicles). 
This avoids enables horizontal scalability.