package com.decay.kafka.cinfig;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AutoKafkaTopicCreateConfig {

    @Value("${kafka.topic}")
    public String topicName;

    @Bean
    public NewTopic newTopic() {
        return TopicBuilder.name(topicName).partitions(3)
                .replicas(3).build();
    }
}
