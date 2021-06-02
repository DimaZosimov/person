package com.person.dao;

import com.person.model.Person;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class PersonDao{

  private Person person;

  public <S extends Person> S save(S entity) {
    return null;
  }

  public <S extends Person> Iterable<S> saveAll(Iterable<S> entities) {
    return null;
  }

  public Optional<Person> findById(Integer integer) {
    return Optional.empty();
  }

  public boolean existsById(Integer integer) {
    return false;
  }

  public Iterable<Person> findAll() {
    return null;
  }

  public Iterable<Person> findAllById(Iterable<Integer> integers) {
    return null;
  }

  public long count() {
    return 0;
  }

  public void deleteById(Integer integer) {

  }

  public void delete(Person entity) {

  }

  public void deleteAllById(Iterable<? extends Integer> integers) {

  }

  public void deleteAll(Iterable<? extends Person> entities) {

  }

  public void deleteAll() {

  }
}
