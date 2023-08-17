package com.example.postoffice.repository;

import com.example.postoffice.model.PostOffice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostOfficeRepository extends JpaRepository<PostOffice, Long> {
    Optional<PostOffice> findPostOfficeByIndex(String index);
}