package org.example.workerservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.workerservice.service.MinioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/file")
@RequiredArgsConstructor
public class MinioController {
    private final MinioService minioService;

    @GetMapping
    public ResponseEntity<Object> getFiles() {
        return ResponseEntity.ok(minioService.getListObjects());
    }

}