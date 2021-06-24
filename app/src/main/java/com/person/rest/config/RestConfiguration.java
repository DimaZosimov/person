package com.person.rest.config;

import com.person.integration.PersonIntegrationConfig;
import com.person.integration.PersonOutputIntegrationConfig;
import com.person.kafka.config.KafkaConsumerConfig;
import com.person.kafka.config.KafkaProducerConfig;
import com.person.testdb.TestDBConfig;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(value = "com.person")
@EnableJpaRepositories(basePackages = "com.person.dao")
@EntityScan(basePackages = "com.person.model")
@Import({TestDBConfig.class,
    SwaggerConfig.class,
    KafkaConsumerConfig.class,
    KafkaProducerConfig.class,
    PersonIntegrationConfig.class,
    PersonOutputIntegrationConfig.class})
public class RestConfiguration {

}
