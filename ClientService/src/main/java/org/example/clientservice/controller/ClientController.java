package org.example.clientservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.clientservice.dto.ClientResponse;
import org.example.clientservice.service.ClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/client-service")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/calls")
    public List<ClientResponse> clientCalls(String number, Date startDate, Date endDate) {
        return clientService.getAllClientsByNumberAndCallDateBetween(number, startDate, endDate).stream()
                .map(client -> ClientResponse.builder()
                        .number(client.getNumber())
                        .dateCall(client.getDateCall())
                        .statusCall(client.getClientStatusCall())
                        .duration(client.getDuration())
                        .build()
                ).collect(Collectors.toList());
    }
}
