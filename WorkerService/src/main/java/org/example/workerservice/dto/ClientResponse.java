package org.example.workerservice.dto;

import lombok.*;
import org.example.workerservice.enums.ClientStatus;

import java.sql.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClientResponse {
    private String number;
    private ClientStatus statusCall;
    private Double duration;
    private Date dateCall;
}
