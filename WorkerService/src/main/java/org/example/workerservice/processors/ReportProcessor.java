package org.example.workerservice.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.workerservice.dto.ClientResponse;
import org.example.workerservice.dto.FileDto;
import org.example.workerservice.entity.Report;
import org.example.workerservice.enums.ReportStatus;
import org.example.workerservice.repository.ReportRepository;
import org.example.workerservice.service.ApiService;
import org.example.workerservice.service.MinioService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportProcessor {

    private final MinioService minioService;
    private final ReportRepository reportRepository;
    private final ApiService apiService;
    private final KafkaTemplate<String, Report> kafkaTemplate;

    @Scheduled(fixedRate = 5000)
    @Async
    public void createReport() {
        System.out.println("Я в WORKER SCHEDULED METHOD");
        var allReportsByStatus = reportRepository.findAllByReportStatus(ReportStatus.PENDING);
        if (!allReportsByStatus.isEmpty()) {
            // Перебирая все отчеты с статусом PENDING, вызываем метод api service ( READ ONLY DB )
            // и начинаем построение отчета. Ответ поступит в виде готового отчета, который требуется
            // сохранить в S3, а ссылку вернуть в report.
            allReportsByStatus.forEach(report -> {
                // Построение готового отчета
                List<ClientResponse> readyReport = apiService.getInformationByNumberAndDate(
                        report.getPhoneNumber(),
                        report.getStartDate(),
                        report.getEndDate());

                FileDto fileDto;
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    var string = mapper.writeValueAsString(readyReport);
                    var bytes = string.getBytes();
                    var byteArrayInputStream = new ByteArrayInputStream(bytes);
                    fileDto = minioService.uploadFile(FileDto.builder()
                            .title(report.getUuid().toString())
                            .filename(report.getUuid().toString())
                            .description("REPORT")
                            .file(byteArrayInputStream)
                            .size((long) byteArrayInputStream.available())
                            .url(report.getUuid().toString())
                            .build());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // Установка ссылке в report
                report.setReference(fileDto.getUrl());
                // Установка статуса в DONE в воркере.
                report.setReportStatus(ReportStatus.DONE);
                // Сохранение измененного report с новым статусом.
                reportRepository.save(report);

                kafkaTemplate.send("master", report.getPhoneNumber(), report);
            });
        }
    }
}
