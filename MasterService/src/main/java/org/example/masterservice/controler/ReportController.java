package org.example.masterservice.controler;

import lombok.RequiredArgsConstructor;
import org.example.masterservice.dto.ReferenceResponse;
import org.example.masterservice.dto.ReportDTO;
import org.example.masterservice.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/master-service")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/report/create")
    public ResponseEntity<ReferenceResponse> createReport(@RequestBody ReportDTO reportDTO) {
        return ResponseEntity.ok()
                .body(reportService.handleReportDelivery(reportDTO));
    }
}
