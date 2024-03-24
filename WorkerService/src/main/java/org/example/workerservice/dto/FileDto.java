package org.example.workerservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import feign.Client;
import lombok.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 232836038145089522L;

    private String title;

    private String description;

    private InputStream file;

    private Long size;

    private String url;

    private String filename;

}