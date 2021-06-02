package com.person.service;

import com.person.model.Person;
import java.util.Optional;

public interface PersonService {

  Iterable<Person> findAll();
  Optional<Person> findById(Integer id);
  Person save(Person person);
  void delete(Person person);


}
