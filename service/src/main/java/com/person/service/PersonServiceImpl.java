package com.person.service;

import com.person.dao.PersonDaoDataJpa;
import com.person.model.Person;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

  private PersonDaoDataJpa personDaoDataJpa;

  public PersonServiceImpl(PersonDaoDataJpa personDaoDataJpa) {
    this.personDaoDataJpa = personDaoDataJpa;
  }

  @Transactional(readOnly = true)
  @Override
  public List<Person> findAll() {
    return null;
  }

  @Transactional(readOnly = true)
  @Override
  public Optional<Person> findById(Integer id) {
    return Optional.empty();
  }

  @Override
  public Person save(Person person) {
    return null;
  }

  @Override
  public Person update(Person person) {
    return null;
  }

  @Override
  public void delete(Person person) {

  }
}
