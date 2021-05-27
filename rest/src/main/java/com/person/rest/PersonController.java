package com.person.rest;

import com.person.model.Person;
import com.person.model.PersonBuilder;
import com.person.service.PersonService;
import java.net.URI;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class PersonController {

  private final PersonService personService;

  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @GetMapping("/")
  public ResponseEntity<Iterable<Person>> findAll() {
    return ResponseEntity.ok(personService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Person> findById(@PathVariable Integer id) {
    Optional<Person> optPerson = personService.findById(id);
    return optPerson.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping(value = "/person")
  public ResponseEntity<Person> save(@RequestBody Person person) {
    Person savedPerson = personService.save(person);
    return generateResponseSavedAndUpdatedPerson(savedPerson);
  }

  @PutMapping(value = "/person")
  public ResponseEntity<Person> update(@RequestBody Person person) {
    Person updatedPerson = personService.update(person);
    return generateResponseSavedAndUpdatedPerson(updatedPerson);
  }

  private ResponseEntity<Person> generateResponseSavedAndUpdatedPerson(Person savedPerson) {
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(savedPerson.getId()).toUri();
    return savedPerson.getId() != null ? ResponseEntity.created(location).build()
        : ResponseEntity.badRequest().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Person> delete(@PathVariable Integer id) {
    personService.delete(PersonBuilder.create().withId(id).build());
    return ResponseEntity.noContent().build();
  }

}
