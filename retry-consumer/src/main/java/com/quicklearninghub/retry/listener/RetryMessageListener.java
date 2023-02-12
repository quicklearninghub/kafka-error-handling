package com.quicklearninghub.retry.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.quicklearninghub.retry.dto.MyDTO;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@NoArgsConstructor
public class RetryMessageListener {

    private ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${kafka.topic}", groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listen(ConsumerRecord<String, String> consumerRecord) {
        log.info("Started consuming message on topic: {}, offset {}, message {}", consumerRecord.topic(),
                consumerRecord.offset(), consumerRecord.value());


        if(consumerRecord.offset() % 2 != 0) throw new IllegalStateException("This is something odd.");

        try {
            MyDTO myDto = objectMapper.readValue(consumerRecord.value(), MyDTO.class);
            log.info("Finished consuming message on topic: {}, offset {}, message {}", consumerRecord.topic(),
                    consumerRecord.offset(), myDto);
            // do something with the deserialized object
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
