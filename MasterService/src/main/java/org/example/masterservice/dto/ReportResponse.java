package org.example.masterservice.dto;

import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReportResponse {
    private UUID uuid;
}
