package org.example.clientservice;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class ClientServiceApplication{
    public static void main(String[] args) {
        SpringApplication.run(ClientServiceApplication.class, args);
    }

}
