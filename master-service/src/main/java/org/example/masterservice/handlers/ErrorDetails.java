package org.example.masterservice.handlers;

import lombok.Builder;
import lombok.Data;
import org.example.masterservice.constants.DateTimeConstants;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


@Data
@Builder
public class ErrorDetails {
    private int status;
    private String error;
    private List<String> message;
    private String timestamp;

    public static ErrorDetails getErrorDetails(HttpStatus httpStatus,
                                               String errorStatus,
                                               Exception exception) {
        return ErrorDetails.builder()
                .status(httpStatus.value())
                .error(errorStatus)
                .timestamp(getDateTimeFormatter())
                .message(Collections.singletonList(exception.getMessage()))
                .build();
    }

    public static String getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(DateTimeConstants.DATE_TIME_FORMAT, Locale.ENGLISH)
                .format(LocalDateTime.now());
    }
}