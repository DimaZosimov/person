package com.person.integration;

import com.person.model.PersonBuilder;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

@Configuration
public class PersonOutputIntegrationConfig {

  @Bean
  public ApplicationRunner runner(MessageChannel input) {
    return args -> {
      input.send(MessageBuilder.withPayload(PersonBuilder.create().withId(1).withName("bill").build())
        .build());
    };
  }
}
