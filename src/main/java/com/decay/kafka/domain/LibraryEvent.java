package com.decay.kafka.domain;

import com.decay.kafka.domain.enums.LibraryEventType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record LibraryEvent(Integer libraryEventId,
                           LibraryEventType libraryEventType,
                           @NotNull
                           @Valid
                           Book book) {
}
