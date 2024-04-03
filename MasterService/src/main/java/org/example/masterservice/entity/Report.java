package org.example.masterservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.masterservice.enums.ReportStatus;

import java.sql.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "report", indexes = {
        @Index(name = "idx_unique_message_key", columnList = "phoneNumber, startDate, endDate")
})
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
