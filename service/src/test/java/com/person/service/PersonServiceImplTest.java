package com.person.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import com.person.dao.PersonDaoDataJpa;
import com.person.model.Person;
import com.person.model.PersonBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Simple unit tests use JUnit5 and Mockito and don`t use Spring. This test uses only one level
 * layer.
 */

@ExtendWith(MockitoExtension.class)
public class PersonServiceImplTest {

  @Mock
  private PersonDaoDataJpa personDaoDataJpa;

  @InjectMocks
  private PersonServiceImpl personService;

  @Test
  public void shouldReturnPersonList() {
    List<Person> dblist = new ArrayList<>();
    dblist.add(PersonBuilder.create().withId(1).withName("Bill").build());
    dblist.add(PersonBuilder.create().withId(2).withName("Den").build());
    dblist.add(PersonBuilder.create().withId(3).withName("Jack").build());

    Mockito.when(personDaoDataJpa.findAll()).thenReturn(dblist);
    List<Person> list = (List<Person>) personService.findAll();
    Assertions.assertNotNull(list);
    Assertions.assertFalse(list.isEmpty());
    Assertions.assertEquals(3, list.size());
    Assertions.assertEquals(dblist, list);

    Mockito.verify(personDaoDataJpa).findAll();
    Mockito.verifyNoMoreInteractions(personDaoDataJpa);
  }

  @Test
  public void shouldReturnPersonById() {
    Person person = PersonBuilder.create().withId(1).withName("Bill").build();
    Optional<Person> opt = Optional.of(person);
    Mockito.when(personDaoDataJpa.findById(anyInt())).thenReturn(opt);
    Optional<Person> foundPerson = personService.findById(1);
    Assertions.assertTrue(foundPerson.isPresent());

    Mockito.verify(personDaoDataJpa).findById(anyInt());
    Mockito.verifyNoMoreInteractions(personDaoDataJpa);
  }

  @Test
  public void shouldSavePerson() {
    Person reqPerson = PersonBuilder.create().withName("Bill").build();
    Person resPerson = PersonBuilder.create().withName("Bill").withId(1).build();

    Mockito.when(personDaoDataJpa.save(any())).thenReturn(resPerson);
    Person savedPerson = personService.save(reqPerson);
    Assertions.assertNotNull(savedPerson);
    Assertions.assertEquals(resPerson, savedPerson);

    Mockito.verify(personDaoDataJpa).save(any());
    Mockito.verifyNoMoreInteractions(personDaoDataJpa);
  }

  @Test
  public void shouldUpdatePerson() {
    Person reqPerson = PersonBuilder.create().withName("Bill").withId(1).build();
    Person resPerson = PersonBuilder.create().withName("John").withId(1).build();

    Mockito.when(personDaoDataJpa.save(any())).thenReturn(resPerson);
    Person savedPerson = personService.save(reqPerson);
    Assertions.assertNotNull(savedPerson);
    Assertions.assertEquals(resPerson, savedPerson);

    Mockito.verify(personDaoDataJpa).save(any());
    Mockito.verifyNoMoreInteractions(personDaoDataJpa);
  }

  @Test
  public void shouldDeletePerson() {
    Person reqPerson = PersonBuilder.create().withName("Bill").withId(1).build();
    personService.delete(reqPerson);
    Mockito.verify(personDaoDataJpa).delete(any());
    Mockito.verifyNoMoreInteractions(personDaoDataJpa);
  }
}
