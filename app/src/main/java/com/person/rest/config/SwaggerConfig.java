package com.person.rest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
            .title("Person Test Application")
            .version("0.0.1-SNAPSHOT")
            .contact(new Contact()
                .url("http://localhost:8070/")
                .name("Zosimov Dima")
            )
        );
  }

}
