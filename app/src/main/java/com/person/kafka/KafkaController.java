package com.person.kafka;

import com.person.model.Person;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("msg")
public class KafkaController {

  private KafkaTemplate<Integer, Person> kafkaTemplate;

  @Autowired
  public KafkaController(KafkaTemplate<Integer, Person> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @PostMapping
  public void sendOrder(Integer msgKey, Person person) {
    //Слушатель для просмотра результатов отправки сообщения
    ListenableFuture<SendResult<Integer, Person>> future = kafkaTemplate
        .send("msg", msgKey, person);
    future.addCallback(System.out::println, System.err::println);
    kafkaTemplate.flush();
  }

  @KafkaListener(topics = "msg", containerFactory = "singleFactory")
  public void orderListener(ConsumerRecord<Integer, Person> record) {
    System.out.println(record.partition());
    System.out.println(record.key());
    System.out.println(record.value());
  }
}
