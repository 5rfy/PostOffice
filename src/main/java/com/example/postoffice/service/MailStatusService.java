package com.example.postoffice.service;

import com.example.postoffice.model.MailItem;
import com.example.postoffice.model.MailStatus;
import com.example.postoffice.repository.MailStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MailStatusService {
    private final MailStatusRepository mailStatusRepository;
    @Transactional
    public void statusChange(MailStatus mailStatus) {
        mailStatusRepository.save(mailStatus);
    }

    public List<MailStatus> findStatusByMailItem(MailItem mailItem) {
        return mailStatusRepository.findByMailItemOrderByTimestamp(mailItem);
    }
}
