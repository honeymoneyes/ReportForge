package org.example.masterservice.handlers;

import org.example.masterservice.exception.EmptyReportListException;
import org.example.masterservice.exception.ReportNotReadyYetException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class Handler {

    @ExceptionHandler(ReportNotReadyYetException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorDetails handleReportNotReadyYetException(ReportNotReadyYetException exception) {
        return ErrorDetails.getErrorDetails(NOT_FOUND, "REFERENCE_NOT_EXIST", exception);
    }

    @ExceptionHandler(EmptyReportListException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorDetails handleEmptyReportListException(EmptyReportListException exception) {
        return ErrorDetails.getErrorDetails(NOT_FOUND, "REPORT_LIST_IS_EMPTY", exception);
    }
}
