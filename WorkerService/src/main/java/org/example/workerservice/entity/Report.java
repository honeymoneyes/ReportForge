package org.example.workerservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.workerservice.enums.ReportStatus;

import java.sql.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID uuid;
    private String phoneNumber;
    private Date startDate;
    private Date endDate;
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;
    private String reference;
}
