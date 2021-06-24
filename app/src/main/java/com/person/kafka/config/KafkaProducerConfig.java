package com.person.kafka.config;

import com.person.model.Person;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String kafkaServer;

  @Value("${spring.kafka.consumer.group-id}")
  private String kafkaGroupId;

  @Value("${spring.kafka.producer.acks}")
  private String acks;

  @Value("${spring.kafka.producer.enable-idempotence}")
  private String enableIdempotence;

  @Value("${spring.kafka.producer.max-in-flight-request-per-connection}")
  private String maxInFlightRequestPerConnection;

  @Value("${spring.kafka.producer.compression-type}")
  private String compressionType;

  @Value("${spring.kafka.producer.linger-ms}")
  private String lingerMs;

  @Value("${spring.kafka.producer.batch-size}")
  private String batchSize;

  @Value("${spring.kafka.producer.buffer-memory}")
  private String bufferMemory;

  @Value("${spring.kafka.producer.max-block-ms}")
  private String maxBlockMs;

  @Bean
  public Map<String, Object> producerConfigs() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaGroupId);
    //the next 4 rows add more safety
    props.put(ProducerConfig.ACKS_CONFIG, acks);
    props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, enableIdempotence);
    props.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
    props
        .put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInFlightRequestPerConnection);
    //high throughput producer
    props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType);
    props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);

    props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
    props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, maxBlockMs);
    return props;
  }

  @Bean
  public ProducerFactory<Integer, Person> producerFactory() {
    return new DefaultKafkaProducerFactory<>(producerConfigs());
  }

  @Bean
  public KafkaTemplate<Integer, Person> kafkaTemplate() {
    KafkaTemplate<Integer, Person> template = new KafkaTemplate<>(producerFactory());
    template.setMessageConverter(new StringJsonMessageConverter());
    return template;
  }

}
