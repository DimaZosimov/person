package com.person.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.person.model.Person;
import com.person.model.PersonBuilder;
import java.time.LocalDate;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * With this annotation, you can test any JPA related parts of your application. A good example is
 * to verify that a native query is working as expected.
 * <p>
 * What's part of the Spring Test Context: {@link Repository}, {@link EntityManager}, {@link
 * TestEntityManager}, {@link DataSource}.
 * <p>
 * What's not part of the Spring Test Context: {@link Service}, {@link Component}, {@link
 * Controller} beans.
 * <p>
 * By default, this annotation tries to auto-configure use an embedded database (e.g. H2) as the
 * DataSource. While an in-memory database might not be a good choice to verify a native query using
 * proprietary features, you can disable this auto-configuration with {@link
 * AutoConfigureTestDatabase} and use e.g. Testcontainers to create a PostgreSQL database for
 * testing.
 * <p>
 * Also see {@link TestApplication}
 */

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Testcontainers
public class PersonJpaDaoDataJpaTest {

  @Autowired
  private DataSource dataSource;

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private PersonDaoDataJpa personDaoDataJpa;

  @Test
  public void shouldReturnAllPerson() {
    Iterable<Person> result = personDaoDataJpa.findAll();
    assertNotNull(result);
    result.forEach(Assertions::assertNotNull);
    assertNotNull(dataSource);
    assertNotNull(entityManager);
  }

  @Test
  public void shouldReturnPersonById() {
    int id = 1;
    Optional<Person> optPersonal = personDaoDataJpa.findById(id);
    assertTrue(optPersonal.isPresent());
    assertEquals(id, optPersonal.get().getId());
    assertNotNull(dataSource);
    assertNotNull(entityManager);
  }

  @Test
  public void shouldNotReturnPersonById() {
    int id = 19999;
    Optional<Person> optPersonal = personDaoDataJpa.findById(id);
    assertTrue(optPersonal.isEmpty());
    assertNotNull(dataSource);
    assertNotNull(entityManager);
  }

  @Test
  public void shouldReturnSavedPerson() {
    Person person = PersonBuilder.create().withName("Dave").build();
    Person savedPerson = personDaoDataJpa.save(person);
    assertNotNull(savedPerson);
    assertNotNull(savedPerson.getId());
    assertEquals("Dave", savedPerson.getName());
    assertEquals(LocalDate.now(), savedPerson.getCreated());
    assertEquals(LocalDate.now(), savedPerson.getModified());
    assertNotNull(dataSource);
    assertNotNull(entityManager);
  }

  @Test
  public void shouldReturnUpdatedPerson() {
    int id = 1;
    Optional<Person> optPersonal = personDaoDataJpa.findById(id);
    assertTrue(optPersonal.isPresent());
    assertEquals(id, optPersonal.get().getId());
    Person person = optPersonal.get();
    person.setName("John");
    person.onUpdate();
    Person updatedPerson = personDaoDataJpa.save(person);

    assertNotNull(updatedPerson);
    assertNotNull(updatedPerson.getId());
    assertEquals("John", updatedPerson.getName());
    assertEquals(person.getCreated(), updatedPerson.getCreated());
    assertEquals(LocalDate.now(), updatedPerson.getModified());

    optPersonal = personDaoDataJpa.findById(id);
    assertTrue(optPersonal.isPresent());
    assertEquals(id, optPersonal.get().getId());
    assertEquals(LocalDate.now(), optPersonal.get().getModified());
    assertNotNull(dataSource);
    assertNotNull(entityManager);
  }

  @Test
  public void shouldDeletePerson() {
    int id = 1;
    Optional<Person> optPersonal = personDaoDataJpa.findById(id);
    assertTrue(optPersonal.isPresent());
    assertEquals(id, optPersonal.get().getId());

    personDaoDataJpa.delete(optPersonal.get());
    optPersonal = personDaoDataJpa.findById(optPersonal.get().getId());
    assertTrue(optPersonal.isEmpty());
    assertNotNull(dataSource);
    assertNotNull(entityManager);
  }
}
/*
  @Testcontainers
  @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
  public class ApplicationIT {

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
        .withPassword("inmemory")
        .withUsername("inmemory");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
      registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
      registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
      registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Test
    public void contextLoads() {
    }
*/
