package com.decay.kafka.domain;

import lombok.Builder;

@Builder
public record ErrorModel(String status, String message) {
}
