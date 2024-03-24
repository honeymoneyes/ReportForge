package org.example.masterservice.repository;

import org.example.masterservice.entity.Report;
import org.example.masterservice.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllByReportStatus(ReportStatus reportStatus);
}
