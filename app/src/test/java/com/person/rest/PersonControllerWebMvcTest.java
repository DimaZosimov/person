package com.person.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.person.model.Person;
import com.person.model.PersonBuilder;
import com.person.service.PersonService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Web layer unit test. What's part of the Spring Test Context: @Controller, @ControllerAdvice,
 * @JsonComponent, Converter, Filter, WebMvcConfigurer. What's not part of the Spring Test Context:
 * @Service, @Component, @Repository beans
 */

@WebMvcTest
public class PersonControllerWebMvcTest {

  private static final String FULL_URL_PERSON = "http://localhost/person/";
  private static final String PERSON_URL = "/person";
  private static final String URL = "/";

  @Autowired
  private PersonController personController;
  private MockMvc mockMvc;
  private ObjectMapper mapper = new ObjectMapper();

  @MockBean
  private PersonService personService;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(personController)
        .setMessageConverters(new MappingJackson2HttpMessageConverter())
        .alwaysDo(MockMvcResultHandlers.print())
        .build();
    MockitoAnnotations.openMocks(this);
    mapper.registerModule(new JavaTimeModule());
  }

  @Test
  public void shouldReturnPersonListAndStatusOk() throws Exception {
    List<Person> list = new ArrayList<>();
    list.add(PersonBuilder.create().withName("Bill").withId(1).withCreated(LocalDate.now())
        .withModified(LocalDate.now()).build());
    list.add(PersonBuilder.create().withName("Jack").withId(2).withCreated(LocalDate.now())
        .withModified(LocalDate.now()).build());
    list.add(PersonBuilder.create().withName("Den").withId(3).withCreated(LocalDate.now())
        .withModified(LocalDate.now()).build());
    when(personService.findAll()).thenReturn(list);

    this.mockMvc.perform(MockMvcRequestBuilders.get(URL))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].id", Matchers.is(1)))
        .andExpect(jsonPath("$[0].name", Matchers.is("Bill")))
        .andExpect(jsonPath("$[1].id", Matchers.is(2)))
        .andExpect(jsonPath("$[1].name", Matchers.is("Jack")))
        .andExpect(jsonPath("$[2].id", Matchers.is(3)))
        .andExpect(jsonPath("$[2].name", Matchers.is("Den")));
  }

  @Test
  public void shouldReturnPersonByIdAndStatusOk() throws Exception {
    Optional<Person> optionalPerson = Optional
        .of(PersonBuilder.create().withName("Bill").withId(1).build());
    when(personService.findById(anyInt())).thenReturn(optionalPerson);
    this.mockMvc.perform(MockMvcRequestBuilders.get(PERSON_URL + "/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("id", Matchers.is(1)))
        .andExpect(jsonPath("name", Matchers.is("Bill")));
  }

  @Test
  public void shouldReturnStatusNotFound() throws Exception {
    Optional<Person> optionalPerson = Optional.empty();
    when(personService.findById(anyInt())).thenReturn(optionalPerson);
    this.mockMvc.perform(MockMvcRequestBuilders.get(PERSON_URL + "/1"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void shouldReturnStatusCreatedAfterSavingPerson() throws Exception {
    Person person = PersonBuilder.create().withName("Jason").withId(1).build();
    String json = mapper.writeValueAsString(person);
    when(personService.save(any())).thenReturn(person);
    MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post(PERSON_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().exists("location"))
        .andReturn().getResponse();
    Assertions.assertNotNull(response);
    Assertions.assertEquals(201, response.getStatus());
  }

  @Test
  public void shouldReturnStatusBadRequestAfterSavingPerson() throws Exception {
    Person person = PersonBuilder.create().build();
    String json = mapper.writeValueAsString(person);
    when(personService.save(any())).thenReturn(person);
    MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post(PERSON_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn().getResponse();
    Assertions.assertNotNull(response);
    Assertions.assertEquals(400, response.getStatus());
  }

  @Test
  public void shouldReturnStatusCreatedAfterUpdatingPerson() throws Exception {
    Person person = PersonBuilder.create().withName("Jason").withId(1).build();
    String json = mapper.writeValueAsString(person);
    when(personService.save(any())).thenReturn(person);
    MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put(PERSON_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().exists("location"))
        .andReturn().getResponse();
    Assertions.assertNotNull(response);
    Assertions.assertEquals(201, response.getStatus());
  }

  @Test
  public void shouldReturnStatusBadRequestAfterUpdatingPerson() throws Exception {
    Person person = PersonBuilder.create().build();
    String json = mapper.writeValueAsString(person);
    when(personService.save(any())).thenReturn(person);
    MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put(PERSON_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn().getResponse();
    Assertions.assertNotNull(response);
    Assertions.assertEquals(400, response.getStatus());
  }

  @Test
  public void shouldReturnNoContentStatusAfterDeletingPerson() throws Exception {
    int id = 1;
    MockHttpServletResponse response = mockMvc
        .perform(MockMvcRequestBuilders.delete(PERSON_URL + "/" + id))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andReturn().getResponse();
    Assertions.assertNotNull(response);
    Assertions.assertEquals(204, response.getStatus());
  }

  @Test
  public void shouldReturnNoContentStatusAfterDeletingPersonWhoNotExists() throws Exception {
    int id = 19999;
    MockHttpServletResponse response = mockMvc
        .perform(MockMvcRequestBuilders.delete(PERSON_URL + "/" + id))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andReturn().getResponse();
    Assertions.assertNotNull(response);
    Assertions.assertEquals(204, response.getStatus());
  }
}
