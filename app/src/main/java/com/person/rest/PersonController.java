package com.person.rest;

import com.person.model.Person;
import com.person.model.PersonBuilder;
import com.person.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.Optional;
import javax.validation.constraints.Min;
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

@Tag(name = "a person controller", description = "execute all crud operations with person")
@RestController
public class PersonController {

  private PersonService personService;

  @Autowired
  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @Operation(summary = "Find all", description = "Allows to find all persons")
  @GetMapping("/")
  public ResponseEntity<Iterable<Person>> findAll() {
    return ResponseEntity.ok(personService.findAll());
  }

  @Operation(summary = "Find by id", description = "Allows to find person by identification")
  @GetMapping("/person/{id}")
  public ResponseEntity<Person> findById(
      @PathVariable @Min(1) @Parameter(description = "a person identifier") Integer id) {
    Optional<Person> optPerson = personService.findById(id);
    return optPerson.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @Operation(summary = "Save and update", description = "Allows to save or update a person")
  @RequestMapping(value = "/person", method = {RequestMethod.POST, RequestMethod.PUT})
  public ResponseEntity<Person> save(
      @RequestBody @Parameter(description = "the request body of the person") Person person) {
    Person savedPerson = personService.save(person);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(savedPerson.getId()).toUri();
    return savedPerson.getId() != null ? ResponseEntity.created(location).build()
        : ResponseEntity.badRequest().build();
  }

  @Operation(summary = "Delete", description = "Allows to delete a person")
  @DeleteMapping("/person/{id}")
  public ResponseEntity<Person> delete(
      @PathVariable @Min(1) @Parameter(description = "a person identifier") Integer id) {
    personService.delete(PersonBuilder.create().withId(id).build());
    return ResponseEntity.noContent().build();
  }

}
