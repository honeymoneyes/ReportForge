package org.example.masterservice.controler;

import lombok.RequiredArgsConstructor;
import org.example.masterservice.dto.ReportDTO;
import org.example.masterservice.dto.ReportResponse;
import org.example.masterservice.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/create")
    public ResponseEntity<ReportResponse> createReport(@RequestBody ReportDTO reportDTO) {
        return ResponseEntity.ok(reportService.createReport(reportDTO));
    }
}
