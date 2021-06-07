package com.person.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.person.model.Person;
import com.person.model.PersonBuilder;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Transactional
public class PersonControllerIT {

  private static final String FULL_URL_PERSON = "http://localhost/person/";
  private static final String PERSON_URL = "/person";
  private static final String URL = "/";

  @Autowired
  private PersonController personController;
  private MockMvc mockMvc;
  private ObjectMapper mapper = new ObjectMapper();

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
    MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(URL)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn().getResponse();
    List<Person> result = (List<Person>) mapper.readValue(response.getContentAsString(),
        new TypeReference<Iterable<Person>>() {
        });
    Assertions.assertFalse(result.isEmpty());
  }

  @Test
  public void shouldReturnPersonByIdAndStatusOk() throws Exception {
    Integer id = 1;
    MockHttpServletResponse response = mockMvc
        .perform(MockMvcRequestBuilders.get(PERSON_URL + "/" + id)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn().getResponse();
    Person person = mapper.readValue(response.getContentAsString(), new TypeReference<Person>() {
    });
    Assertions.assertNotNull(person);
    Assertions.assertEquals(id, person.getId());
  }

  @Test
  public void shouldReturnStatusNotFound() throws Exception {
    int id = 1999999;
    MockHttpServletResponse response = mockMvc
        .perform(MockMvcRequestBuilders.get(PERSON_URL + "/" + id)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn().getResponse();
    Assertions.assertNotNull(response);
  }

  @Test
  public void shouldReturnStatusCreatedAfterSavingPerson() throws Exception {
    Person person = PersonBuilder.create().withName("Jason").build();
    String json = mapper.writeValueAsString(person);
    // first request -> save a new person
    MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post(PERSON_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().exists("location"))
        .andReturn().getResponse();
    Assertions.assertNotNull(response);
    Assertions.assertEquals(201, response.getStatus());

    // second request -> check if this person is saved correctly
    Integer id = getIdFromLocation(response);
    response = mockMvc.perform(MockMvcRequestBuilders.get(PERSON_URL + "/" + id)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn().getResponse();
    Person savedPerson = mapper
        .readValue(response.getContentAsString(), new TypeReference<Person>() {
        });
    Assertions.assertNotNull(savedPerson);
    Assertions.assertEquals(id, savedPerson.getId());
    Assertions.assertEquals(person.getName(), savedPerson.getName());
    Assertions.assertEquals(LocalDate.now(), savedPerson.getModified());
    Assertions.assertEquals(LocalDate.now(), savedPerson.getCreated());
  }

  private Integer getIdFromLocation(MockHttpServletResponse response) {
    String location = response.getHeader("location");
    return location != null ? Integer.parseInt(location.substring(FULL_URL_PERSON.length()))
        : null;
  }

  //TODO: validate request person
  @Disabled
  @Test
  public void shouldReturnStatusBadRequestAfterSavingPerson() throws Exception {
    Person person = PersonBuilder.create().build();
    String json = mapper.writeValueAsString(person);

    MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post(PERSON_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(MockMvcResultMatchers.status().is4xxClientError())
        .andReturn().getResponse();
    Assertions.assertNotNull(response);
    Assertions.assertEquals(401, response.getStatus());
  }

  @Test
  public void shouldReturnStatusCreatedAfterUpdatingPerson() throws Exception {
    // first request -> find a person by id for next updating
    Integer id = 1;
    MockHttpServletResponse response = mockMvc
        .perform(MockMvcRequestBuilders.get(PERSON_URL + "/" + id)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn().getResponse();
    Person person = mapper.readValue(response.getContentAsString(), new TypeReference<Person>() {
    });
    Assertions.assertNotNull(person);
    Assertions.assertEquals(id, person.getId());
    person.setName("Jason");
    String json = mapper.writeValueAsString(person);

    // second request -> update a person
    response = mockMvc.perform(MockMvcRequestBuilders.put(PERSON_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().exists("location"))
        .andReturn().getResponse();
    Assertions.assertNotNull(response);
    Assertions.assertEquals(201, response.getStatus());

    // third request -> check if this person is updated correctly
    id = getIdFromLocation(response);
    response = mockMvc.perform(MockMvcRequestBuilders.get(PERSON_URL + "/" + id)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn().getResponse();
    Person savedPerson = mapper
        .readValue(response.getContentAsString(), new TypeReference<Person>() {
        });
    Assertions.assertNotNull(savedPerson);
    Assertions.assertEquals(id, savedPerson.getId());
    Assertions.assertEquals(person.getName(), savedPerson.getName());
    Assertions.assertEquals(LocalDate.now(), savedPerson.getModified());
    Assertions.assertNotEquals(LocalDate.now(), savedPerson.getCreated());
  }

  //TODO: validate request person
  @Disabled
  @Test
  public void shouldReturnStatusBadRequestAfterUpdatingPerson() throws Exception {
    Person person = PersonBuilder.create().withId(1).withName("").build();
    String json = mapper.writeValueAsString(person);

    MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post(PERSON_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(MockMvcResultMatchers.status().is4xxClientError())
        .andExpect(MockMvcResultMatchers.header().exists("location"))
        .andReturn().getResponse();
    Assertions.assertNotNull(response);
    Assertions.assertEquals(401, response.getStatus());
  }

  @Test
  public void shouldReturnNoContentStatusAfterDeletingPerson() throws Exception {
    // first request -> delete a person
    Integer id = 1;
    MockHttpServletResponse response = mockMvc
        .perform(MockMvcRequestBuilders.delete(PERSON_URL + "/" + id))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andReturn().getResponse();
    Assertions.assertNotNull(response);

    //second request -> check if this person is deleted
    response = mockMvc.perform(MockMvcRequestBuilders.get(PERSON_URL + "/" + id)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn().getResponse();
  }

  @Test
  public void shouldReturnNoContentStatusAfterDeletingPersonWhoNotExists() throws Exception {
    // first request -> delete a person
    Integer id = 199999;
    MockHttpServletResponse response = mockMvc
        .perform(MockMvcRequestBuilders.delete(PERSON_URL + "/" + id))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andReturn().getResponse();
    Assertions.assertNotNull(response);
  }

}
