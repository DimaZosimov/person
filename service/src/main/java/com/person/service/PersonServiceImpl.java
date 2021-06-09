package com.person.service;

import com.person.dao.PersonDaoDataJpa;
import com.person.model.Person;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("personServiceImpl")
@Transactional
public class PersonServiceImpl implements PersonService {

  private final PersonDaoDataJpa personDaoDataJpa;

  @Autowired
  public PersonServiceImpl(PersonDaoDataJpa personDaoDataJpa) {
    this.personDaoDataJpa = personDaoDataJpa;
  }

  @Transactional(readOnly = true)
  @Override
  public Iterable<Person> findAll() {
    return personDaoDataJpa.findAll();
  }

  @Transactional(readOnly = true)
  @Override
  public Optional<Person> findById(Integer id) {
    return personDaoDataJpa.findById(id);
  }

  @Override
  public Person save(Person person) {
    if (person.getId() != null) {
      person.onUpdate();
    }
    return personDaoDataJpa.save(person);
  }

  @Override
  public void delete(Person person) {
    personDaoDataJpa.delete(person);
  }
}
