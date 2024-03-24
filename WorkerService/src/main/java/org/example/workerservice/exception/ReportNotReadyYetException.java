package org.example.workerservice.exception;

import lombok.Getter;

@Getter
public class ReportNotReadyYetException extends Exception {
    public ReportNotReadyYetException(String message) {
        super(message);
    }
}
