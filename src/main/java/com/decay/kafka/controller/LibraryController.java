package com.decay.kafka.controller;

import com.decay.kafka.domain.LibraryEvent;
import com.decay.kafka.producer.LibraryEventsProducerImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.decay.kafka.domain.enums.LibraryEventType.NEW;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LibraryController {

    private final LibraryEventsProducerImpl libraryEventsProducerImpl;

    @PostMapping("/library-events-async")
    public ResponseEntity<?> postAsyncLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) {
        log.info("Async: Before Publishing");
        libraryEventsProducerImpl.publishMessageAsynchronously(libraryEvent);
        log.info("Async: After Publishing");
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    @PostMapping("/library-events-sync")
    public ResponseEntity<?> postLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) {
        log.info("Sync: Before Publishing");
        if (libraryEvent.libraryEventId() != null && NEW.equals(libraryEvent.libraryEventType())) {
            throw new RuntimeException("Incompatable NEW event type for id " + libraryEvent.libraryEventId());
        }
        libraryEventsProducerImpl.publishMessageSynchronously(libraryEvent);
        log.info("Sync:After Publishing");
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }
}
