package com.decay.kafka.producer;

import com.decay.kafka.domain.LibraryEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class LibraryEventsProducerImpl implements LibraryEventsProducer {

    @Value("${kafka.topic}")
    public String topicName;

    private final KafkaTemplate<Integer, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publishMessageAsynchronously(LibraryEvent libraryEvent) {
        log.info("Publishing");
        try {
            String libraryEventData = objectMapper.writeValueAsString(libraryEvent);
            CompletableFuture<SendResult<Integer, String>> completableFuture = kafkaTemplate.send(topicName,
                    libraryEvent.libraryEventId(), libraryEventData);
            completableFuture.whenComplete((result, exception) ->
                    Optional.ofNullable(exception).ifPresentOrElse(ex -> {
                        log.error("Exception occurred while publishing {}", ex.getMessage());
                        throw new RuntimeException(ex);
                    }, () -> {
                        RecordMetadata recordMetadata = result.getRecordMetadata();
                        String message = "The message is published key {}, value {}, " +
                                "Offset:{}, Partition:{}, Timestamp:{}";
                        log.info(message, libraryEvent.libraryEventId(), result.getProducerRecord().value(),
                                +recordMetadata.offset(), recordMetadata.partition(),
                                recordMetadata.timestamp());
                    }));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void publishMessageSynchronously(LibraryEvent libraryEvent) {

        try {
            String libraryEventData = objectMapper.writeValueAsString(libraryEvent);
            CompletableFuture<SendResult<Integer, String>> completableFuture = kafkaTemplate.send(topicName,
                    libraryEvent.libraryEventId(), libraryEventData);
            SendResult<Integer, String> result = completableFuture.get();
            RecordMetadata recordMetadata = result.getRecordMetadata();
            String message = "The message is published key {}, value {}, " +
                    "Offset:{}, Partition:{}, Timestamp:{}";
            log.info(message, libraryEvent.libraryEventId(), result.getProducerRecord().value(),
                    +recordMetadata.offset(), recordMetadata.partition(),
                    recordMetadata.timestamp());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
