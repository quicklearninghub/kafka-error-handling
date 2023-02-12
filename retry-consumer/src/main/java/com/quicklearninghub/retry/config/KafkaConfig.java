package com.quicklearninghub.retry.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quicklearninghub.retry.entity.FailedMessage;
import com.quicklearninghub.retry.repository.FailedMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@Slf4j
public class KafkaConfig {

    private KafkaProps kafkaProps;

    private final FailedMessageRepository failedMessageRepository;

    private final ObjectMapper objectMapper;

    KafkaConfig(KafkaProps kafkaProps, ObjectMapper objectMapper, FailedMessageRepository failedMessageRepository) {
        this.kafkaProps = kafkaProps;
        this.objectMapper = objectMapper;
        this.failedMessageRepository = failedMessageRepository;
    }

    @Bean("kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Object, Object> concurrentKafkaListenerContainerFactory
                = new ConcurrentKafkaListenerContainerFactory<>();
        concurrentKafkaListenerContainerFactory.setConsumerFactory(new DefaultKafkaConsumerFactory(kafkaProps.consumerProps()));
        concurrentKafkaListenerContainerFactory.setCommonErrorHandler(getDefaultErrorHandler());
        return concurrentKafkaListenerContainerFactory;
    }

    private DefaultErrorHandler getDefaultErrorHandler() {
        return new DefaultErrorHandler((record, exception) -> {
            FailedMessage failedMessageEntity = new FailedMessage();
            try {
                failedMessageEntity.setMessage(objectMapper.writeValueAsString(record.value()));
                failedMessageEntity.setException(exception.getClass().toString());
                failedMessageEntity.setTopic(record.topic());
                failedMessageEntity.setConsumerOffset(record.offset());
                failedMessageRepository.save(failedMessageEntity);
                log.error("Saved the failed message to db {}", failedMessageEntity);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }, new FixedBackOff(10000L, 2L));
    }

}
