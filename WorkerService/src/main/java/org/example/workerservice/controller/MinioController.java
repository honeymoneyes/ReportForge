package org.example.workerservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.workerservice.dto.ClientResponse;
import org.example.workerservice.exception.ReportNotReadyYetException;
import org.example.workerservice.service.MinioService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.servlet.HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE;


@RestController
@RequestMapping(value = "/file")
@RequiredArgsConstructor
public class MinioController {
    private final MinioService minioService;

    @GetMapping
    public ResponseEntity<Object> getFiles() {
        return ResponseEntity.ok(minioService.getListObjects());
    }
    @GetMapping("/**")
    public ResponseEntity<List<ClientResponse>> getFile(HttpServletRequest request) throws ReportNotReadyYetException {
        String pattern = (String) request.getAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE);
        String filename = new AntPathMatcher().extractPathWithinPattern(pattern, request.getServletPath());
        var clientResponses = minioService.downloadFile(filename);

        if (clientResponses == null) {
            throw new ReportNotReadyYetException("Report's reference not exist yet. \n Touch again.");
        }

        return ResponseEntity.ok()
                .body(minioService.downloadFile(filename));
    }
}