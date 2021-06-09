package com.person.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

/**
 * @JsoTest helps to test JSON serialization.
 * <p>
 * What's part of the Spring Test Context: @JsonComponent,ObjectMapper, Module from Jackson or
 * similar components when using JSONB or GSON
 * <p>
 * What's not part of the Spring Test Context: @Service, @Component, @Controller, @Repository
 */
@JsonTest
public class PersonJsonTest {

  @Autowired
  private JacksonTester<Person> jacksonTester;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void shouldSerializeObject() throws IOException {
    Person person = PersonBuilder.create().withName("Bill").withId(1).withCreated(LocalDate.now())
        .withModified(LocalDate.now()).build();

    JsonContent<Person> result = jacksonTester.write(person);

    assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(person.getId());
    assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(person.getName());
    assertThat(result).extractingJsonPathStringValue("$.created")
        .isEqualTo(person.getCreated().toString());
    assertThat(result).extractingJsonPathStringValue("$.modified")
        .isEqualTo(person.getModified().toString());
  }

}
