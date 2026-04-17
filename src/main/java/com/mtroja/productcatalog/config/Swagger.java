package com.mtroja.productcatalog.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger {

    @Bean
    public OpenAPI productCatalogOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product Catalog API")
                        .description("REST API for managing producers and products")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Michał Trojanowski")
                                .email("mtroja98@gmail.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project README"));
    }
}