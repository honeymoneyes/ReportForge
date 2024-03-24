package org.example.workerservice.handlers;


import org.example.workerservice.exception.ReportNotReadyYetException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.example.workerservice.handlers.ErrorDetails.getErrorDetails;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class Handler {

    @ExceptionHandler(ReportNotReadyYetException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorDetails handleReportNotReadyYetException(ReportNotReadyYetException exception) {
        return getErrorDetails(NOT_FOUND, "REFERENCE_NOT_EXIST", exception);
    }
}
