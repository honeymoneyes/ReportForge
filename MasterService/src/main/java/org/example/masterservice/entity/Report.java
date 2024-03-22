package org.example.masterservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.masterservice.enums.Status;

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
    private String firstname;
    private String lastname;
    private Date startDate;
    private Date endDate;
    private Status status;
    private String reference;
}
