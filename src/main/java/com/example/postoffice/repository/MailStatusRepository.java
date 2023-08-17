package com.example.postoffice.repository;

import com.example.postoffice.model.MailItem;
import com.example.postoffice.model.MailStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailStatusRepository extends JpaRepository<MailStatus, Long> {
    List<MailStatus> findByMailItemOrderByTimestamp(MailItem mailItem);
}