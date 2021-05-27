package com.person.service;

import com.person.model.Person;
import java.util.List;
import java.util.Optional;

public interface PersonService {

  List<Person> findAll();
  Optional<Person> findById(Integer id);
  Person save(Person person);
  Person update(Person person);
  void delete(Person person);


}
