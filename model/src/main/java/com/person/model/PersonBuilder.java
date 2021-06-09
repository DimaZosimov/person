package com.person.model;

import java.time.LocalDate;

public class PersonBuilder {

  private static PersonBuilder instance = new PersonBuilder();
  private Person person;

  private PersonBuilder() {
    this.person = new Person();
  }

  public static PersonBuilder create() {
    return instance;
  }

  public PersonBuilder withId(Integer id) {
    this.person.setId(id);
    return instance;
  }

  public PersonBuilder withName(String name) {
    this.person.setName(name);
    return instance;
  }

  public PersonBuilder withCreated(LocalDate created) {
    this.person.setCreated(created);
    return instance;
  }

  public PersonBuilder withModified(LocalDate modified) {
    this.person.setModified(modified);
    return instance;
  }

  public Person build() {
    Person person = new Person();
    person.setName(this.person.getName());
    person.setId(this.person.getId());
    person.setCreated(this.person.getCreated());
    person.setModified(this.person.getModified());
    this.person = new Person();
    return person;
  }

}
