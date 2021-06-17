package com.person.integration;

import com.person.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.handler.LoggingHandler.Level;

@EnableIntegration
@Configuration
public class PersonIntegrationConfig {

  @Bean
  public DirectChannel input() {
    return MessageChannels.direct().get();
  }

  @Bean
  public DirectChannel toTransform() {
    return MessageChannels.direct().get();
  }

  @Bean
  public DirectChannel toLog() {
    return MessageChannels.direct().get();
  }

  @MessageEndpoint
  class SimpleFilter {
    @Filter(inputChannel = "input", outputChannel = "toTransform")
    public boolean process(Person message) {
      return message.getId() != null;
    }
  }

  @MessageEndpoint
  class SimpleTransformer {
    @Transformer(inputChannel = "toTransform", outputChannel = "toLog")
    public String process(Person message) {
      return message.getName().toUpperCase();
    }
  }

  @Bean
  @ServiceActivator(inputChannel = "toLog")
  public LoggingHandler loggingHandler() {
    LoggingHandler handler = new LoggingHandler(Level.INFO);
    handler.setLoggerName("Spring_Integration_Logger");
    handler.setLogExpressionString("headers.id + ': ' + payload");
    return handler;
  }
// вариант вместо Bean loggingHandler
//  @MessageEndpoint
//  class SimpleServiceActivator {
//    Logger log = LoggerFactory.getLogger(SimpleServiceActivator.class);
//    @ServiceActivator(inputChannel = "toLog")
//    public void process(String message) {
//      log.info("Spring Integration message: " + message);
//    }
//  }

}
