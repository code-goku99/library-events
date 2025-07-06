package com.decay.kafka.producer;

import com.decay.kafka.domain.LibraryEvent;

public interface LibraryEventsProducer {

    void publishMessageAsynchronously(LibraryEvent libraryEvent);
    void publishMessageSynchronously(LibraryEvent libraryEvent);

}
