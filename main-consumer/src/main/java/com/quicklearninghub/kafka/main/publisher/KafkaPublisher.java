package com.quicklearninghub.kafka.main.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quicklearninghub.kafka.main.dto.MyDTO;
import com.quicklearninghub.kafka.main.enums.EventType;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class KafkaPublisher {

    @Value(value = "${kafka.topic}")
    private String topic;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(MyDTO dto) {
        try {
            String message = objectMapper.writeValueAsString(dto);
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, dto.getEventType().name(), message);
            getHeaders(dto.getEventType()).forEach((k, v) -> {
                producerRecord.headers().add(k, v.getBytes(StandardCharsets.UTF_16));
            });
            kafkaTemplate.send(producerRecord);
            log.info("Message sent {}", dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> getHeaders(EventType eventType) {
        Map<String, String> headers = new HashMap<>();
        headers.put("eventType", eventType.getEvent());
        return headers;
    }
}
