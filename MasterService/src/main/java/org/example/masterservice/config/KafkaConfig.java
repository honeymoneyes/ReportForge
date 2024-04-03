package org.example.masterservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.example.masterservice.enums.KafkaTopics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(KafkaTopics.WORKER_1)
                .build();
    }
}