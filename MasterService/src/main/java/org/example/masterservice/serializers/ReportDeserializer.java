package org.example.masterservice.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.example.masterservice.entity.Report;

import java.io.IOException;

public class ReportDeserializer implements Deserializer<Report> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Report deserialize(String topic, byte[] data) {
        try {
            return mapper.readValue(data, Report.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
