package org.example.masterservice.exception;

import lombok.Getter;

@Getter
public class ReportNotReadyYetException extends Exception {
    public ReportNotReadyYetException(String message) {
        super(message);
    }
}
