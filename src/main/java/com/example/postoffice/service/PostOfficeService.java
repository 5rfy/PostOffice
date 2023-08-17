package com.example.postoffice.service;

import com.example.postoffice.model.PostOffice;
import com.example.postoffice.repository.PostOfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostOfficeService {
    private final PostOfficeRepository postOfficeRepository;
    public Optional<PostOffice> findPostOffice(String postIndex) {
        return postOfficeRepository.findPostOfficeByIndex(postIndex);
    }
}
