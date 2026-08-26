package com.echeq.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Echeq Management System API")
                        .version("1.0.0")
                        .description("Documentación de la API del sistema de gestión de eCheqs")
                        .contact(new Contact()
                                .name("Equipo Backend")
                                .email("backend@echeq.com")
                        )
                );
    }
}