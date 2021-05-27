package com.person.model;

public class PersonBuilder {

  private static PersonBuilder instance = new PersonBuilder();
  private Integer id = null;

  private PersonBuilder() {

  }

  public static PersonBuilder create() {
    return instance;
  }

  public PersonBuilder withId(Integer id) {
    this.id = id;
    return instance;
  }

  public Person build() {
    Person person = new Person();
    if (id != null) {
      person.setId(id);
    }
    return person;
  }

}
