package com.person.dao;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * This class is to need for {@link com.person.dao.PersonJpaDaoDataJpaTest}. Because our
 * configuration app is in another module.
 */

@SpringBootApplication
@EntityScan(basePackages = "com.person.model")
public class TestApplication {

}
