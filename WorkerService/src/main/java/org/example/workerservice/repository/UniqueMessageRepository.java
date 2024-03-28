package org.example.workerservice.repository;

import org.example.workerservice.entity.UniqueMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniqueMessageRepository extends JpaRepository<UniqueMessage, Long> {
    Optional<UniqueMessage> findUniqueMessageByUniqueKey(String key);
}
