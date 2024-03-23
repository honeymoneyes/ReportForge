package org.example.workerservice.processors;

import lombok.RequiredArgsConstructor;
import org.example.workerservice.entity.Report;
import org.example.workerservice.enums.ReportStatus;
import org.example.workerservice.repository.ReportRepository;
import org.example.workerservice.service.ApiService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

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
            // Построение готового отчета
            var readyReport = apiService.getInformationByNumberAndDate(
                    report.getPhoneNumber(),
                    report.getStartDate(),
                    report.getEndDate());
            // todo Добавить логику добавления отчета в S3 и передать ссылку на отчет
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(readyReport);
                var byteArray = bos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // todo Вместо AWS S3 добавить логику отдельного feign client, который будет являться альтернативой
            // todo базы данных и хранить uuid и массив bytes
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
