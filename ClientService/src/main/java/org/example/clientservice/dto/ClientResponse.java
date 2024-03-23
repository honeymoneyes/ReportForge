package org.example.clientservice.dto;

import lombok.*;
import org.example.clientservice.enums.ClientStatus;

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
