package com.person.rest;

import com.person.model.Person;
import com.person.model.PersonBuilder;
import com.person.service.PersonService;
import java.net.URI;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class PersonController {

  private PersonService personService;

  @Autowired
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

  @RequestMapping(value = "/person", method = {RequestMethod.POST, RequestMethod.PUT})
  public ResponseEntity<Person> save(@RequestBody Person person) {
    Person savedPerson = personService.save(person);
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
