package com.person.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class PersonBuilderTest {

  @Test
  public void shouldBuildPerson() {
    Person person = PersonBuilder.create().withId(1).withName("Test").withCreated(LocalDate.now())
        .withModified(LocalDate.now()).build();
    assertEquals(1, person.getId());
    assertEquals("Test", person.getName());
    assertEquals(LocalDate.now(), person.getCreated());
    assertEquals(LocalDate.now(), person.getModified());

    Person person2 = PersonBuilder.create().build();
    assertNotEquals(person, person2);
  }

}
