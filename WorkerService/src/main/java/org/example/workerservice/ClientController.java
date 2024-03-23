package org.example.workerservice;

import lombok.RequiredArgsConstructor;
import org.example.workerservice.dto.ClientResponse;
import org.example.workerservice.service.ApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/worker-service")
@RequiredArgsConstructor
public class ClientController {
    private final ApiService apiService;

    @GetMapping("/test")
    public List<ClientResponse> test() {
        var allClientsByNumberAndCallDateBetween = apiService.getAllClientsByNumberAndCallDateBetween("+7(918)548-00-11",
                Date.valueOf("2024-02-12"),
                Date.valueOf("2024-02-18"));
        System.out.println();

        return allClientsByNumberAndCallDateBetween;
    }
}
