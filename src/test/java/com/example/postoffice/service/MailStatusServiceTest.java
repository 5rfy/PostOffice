package com.example.postoffice.service;

import com.example.postoffice.model.MailItem;
import com.example.postoffice.model.MailStatus;
import com.example.postoffice.model.PostOffice;
import com.example.postoffice.repository.MailStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MailStatusServiceTest {

    @Mock
    private MailStatusRepository mailStatusRepository;

    @InjectMocks
    private MailStatusService mailStatusService;
    MailItem mailItem;
    PostOffice postOffice;
    List<MailStatus> expectedStatuses;
    MailStatus mailStatus;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mailItem = new MailItem();
        postOffice = new PostOffice();
        expectedStatuses = new ArrayList<>();
        expectedStatuses.add(new MailStatus(1L,mailItem, postOffice, "Arrived", LocalDateTime.now()));
        mailStatus = new MailStatus();
    }

    @Test
    void statusChange() {
        mailStatusService.statusChange(mailStatus);

        verify(mailStatusRepository, times(1)).save(mailStatus);
    }

    @Test
    void shouldReturnStatusByMailItem() {
        when(mailStatusRepository.findByMailItemOrderByTimestamp(mailItem)).thenReturn(expectedStatuses);

        List<MailStatus> actualStatuses = mailStatusService.findStatusByMailItem(mailItem);

        assertEquals(expectedStatuses.size(), actualStatuses.size());
        assertEquals(expectedStatuses.get(0).getStatus(), actualStatuses.get(0).getStatus());

        verify(mailStatusRepository, times(1)).findByMailItemOrderByTimestamp(mailItem);
    }
}