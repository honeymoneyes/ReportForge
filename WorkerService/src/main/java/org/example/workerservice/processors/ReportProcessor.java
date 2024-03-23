package org.example.workerservice.processors;

import lombok.RequiredArgsConstructor;
import org.example.workerservice.dto.ClientResponse;
import org.example.workerservice.entity.Report;
import org.example.workerservice.enums.ReportStatus;
import org.example.workerservice.repository.ReportRepository;
import org.example.workerservice.service.ApiService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportProcessor {

    private final ReportRepository reportRepository;
    private final ApiService apiService;
    private final KafkaTemplate<String, Report> kafkaTemplate;

    @Scheduled(fixedRate = 5000)
    private void createReport() {
        var allReportsByStatus = reportRepository.findAllByReportStatus(ReportStatus.PENDING);
        // Перебирая все отчеты с статусом PENDING, вызываем метод api service ( READ ONLY DB )
        // и начинаем построение отчета. Ответ поступит в виде готового отчета, который требуется
        // сохранить в S3, а ссылку вернуть в report.
        allReportsByStatus.forEach(report -> {
            var readyReport = apiService.getInformationByNumberAndDate(
                    report.getPhoneNumber(),
                    report.getStartDate(),
                    report.getEndDate());
            // todo Добавить логику добавления отчета в S3 и передать ссылку на отчет
            // Установка ссылке в report
            report.setReference("reference");
            // Установка статуса в DONE в воркере.
            report.setReportStatus(ReportStatus.DONE);
            // Сохранение измененного report с новым статусом.
            reportRepository.save(report);
            kafkaTemplate.send("master", report.getPhoneNumber(), report);
        });
    }
}
