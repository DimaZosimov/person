package com.person.cloudstream;

import com.person.model.Person;
import java.time.LocalDate;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.SendTo;

public class PersonProcessor {
  private Logger log = LoggerFactory.getLogger(PersonProcessor.class);

  @Bean
  public Function<Person, Person> transformToUpperCase() {
    return message -> {
      log.info("Processing >>> {}", message);
      Person result = message;
      result.setName(message.getName().toUpperCase());
      result.setModified(LocalDate.now());
      log.info("Message is processed >>> {}", result);
      return result;
    };
  }
}
