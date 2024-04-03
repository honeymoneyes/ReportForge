package org.example.masterservice.controler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.masterservice.dto.ClientResponse;
import org.example.masterservice.dto.ReferenceResponse;
import org.example.masterservice.dto.ReportDTO;
import org.example.masterservice.exception.EmptyReportListException;
import org.example.masterservice.exception.ReportNotReadyYetException;
import org.example.masterservice.service.MinioService;
import org.example.masterservice.service.ReportHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.servlet.HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE;

@RestController
@RequestMapping("/master-service")
@RequiredArgsConstructor
public class ReportController {
    private final ReportHandler reportHandler;
    private final MinioService minioService;

    @PostMapping("/report/create")
    public ResponseEntity<ReferenceResponse> createReport(@RequestBody ReportDTO reportDTO) {
        return ResponseEntity.ok()
                .body(reportHandler.handleReportDelivery(reportDTO));
    }

    @GetMapping("/report/files")
    public ResponseEntity<Object> getFiles() throws EmptyReportListException {
        var listObjects = minioService.getListObjects();
        if (listObjects.isEmpty()) {
            throw new EmptyReportListException("List reports is empty");
        }
        return ResponseEntity.ok(minioService.getListObjects());
    }

    @GetMapping("/report/files/**")
    public ResponseEntity<List<ClientResponse>> getFile(HttpServletRequest request) throws ReportNotReadyYetException {
        String pattern = (String) request.getAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE);
        String filename = new AntPathMatcher().extractPathWithinPattern(pattern, request.getServletPath());
        var clientResponses = minioService.downloadFile(filename);

        if (clientResponses == null) {
            throw new ReportNotReadyYetException("Report's reference not exist yet. Touch again.");
        }

        return ResponseEntity.ok()
                .body(minioService.downloadFile(filename));
    }
}
