package org.example.clientservice.service;

import lombok.RequiredArgsConstructor;
import org.example.clientservice.entity.Client;
import org.example.clientservice.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public List<Client> getAllClientsByNumberAndCallDateBetween(String number, Date startDate, Date endDate) {
        return clientRepository.getClientsByNumberAndDateCallBetween(number, startDate, endDate);
    }
}
