package org.example.workerservice.repository;

import org.example.workerservice.entity.Report;
import org.example.workerservice.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByReportStatus(ReportStatus reportStatus);
}
