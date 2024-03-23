package org.example.clientservice.repository;

import org.example.clientservice.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> getClientsByNumberAndDateCallBetween(String number, Date startDate, Date endDate);
}
