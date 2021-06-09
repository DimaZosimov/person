package com.person.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * What's part of the Spring Test Context: JdbcTemplate, DataSource.
 * <p>
 * What's not part of the Spring Test Context: @Service, @Component, @Controller, @Repository
 * beans.
 */

@JdbcTest
public class PersonDaoJdbcTest {

  @Autowired
  private DataSource dataSource;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  public void shouldReturnBooks() {
    assertNotNull(dataSource);
    assertNotNull(jdbcTemplate);
  }

}
