package com.person.cloudstream;

import com.person.model.Person;
import com.person.model.PersonBuilder;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.support.MessageBuilder;

public class PersonSource {

  @Bean
  @InboundChannelAdapter(channel = Source.OUTPUT, poller = @Poller(fixedDelay = "5000", maxMessagesPerPoll = "5"))
  public MessageSource<Person> simplePerson() {
    return () -> MessageBuilder
        .withPayload(PersonBuilder.create().withId(1).withName("Bill").build())
        .build();
  }
}
