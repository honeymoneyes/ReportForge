package org.example.masterservice.exception;

import lombok.Getter;

@Getter
public class EmptyReportListException extends Exception {
    public EmptyReportListException(String message) {
        super(message);
    }
}
