package com.teleport.tracking.system.rest.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI trackingSystemOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Teleport Tracking System API")
            .description("Spring WebFlux reactive API for the Teleport Tracking System")
            .version("v1.0.0")
            .contact(new Contact()
                .name("Jaysy Ansharullah (Developer)")
                .email("jaysyanshar98@gmail.com"))
            .license(new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0")));
  }
}
