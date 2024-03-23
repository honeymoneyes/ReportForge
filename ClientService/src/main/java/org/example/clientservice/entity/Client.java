package org.example.clientservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.example.clientservice.enums.ClientStatus;

import java.sql.Date;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Client {
    @Id
    private Long id;
    private String number;
    private ClientStatus clientStatusCall;
    private Double duration;
    private Date dateCall;
}
