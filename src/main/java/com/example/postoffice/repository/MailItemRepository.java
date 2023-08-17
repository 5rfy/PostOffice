package com.example.postoffice.repository;

import com.example.postoffice.model.MailItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailItemRepository extends JpaRepository<MailItem, Long> {
}