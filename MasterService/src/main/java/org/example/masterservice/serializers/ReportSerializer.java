package org.example.masterservice.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.example.masterservice.entity.Report;

public class ReportSerializer implements Serializer<Report> {
    private static final ObjectMapper mapper = new ObjectMapper();
    @Override
    public byte[] serialize(String topic, Report data) {
        try {
            return mapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
