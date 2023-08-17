package com.example.postoffice.service;

import com.example.postoffice.dto.MailItemRequest;
import com.example.postoffice.dto.MailStatusResponse;
import com.example.postoffice.model.MailItem;
import com.example.postoffice.model.MailStatus;
import com.example.postoffice.model.PostOffice;
import com.example.postoffice.repository.MailItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MailItemServiceTest {
    @Mock
    private MailItemRepository mailItemRepository;
    @Mock
    private PostOfficeService postOfficeService;
    @Mock
    private MailStatusService mailStatusService;

    @InjectMocks
    private MailItemService mailItemService;

    MailItemRequest request;
    ResponseEntity<String> response;
    MailItem mailItem;
    PostOffice postOffice;
    ResponseEntity<List<MailStatusResponse>> responseListOfMailStatus;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        request = new MailItemRequest();
        mailItem = new MailItem();
        postOffice = new PostOffice();
    }

    @Test
    void shouldRegisterNewMail() {
        request.setType("mail");

        response = mailItemService.registerNewMail(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().startsWith("Mail item registered with id:"));
    }

    @Test
    void shouldRegisterNewBox() {
        request.setType("box");

        response = mailItemService.registerNewMail(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().startsWith("Mail item registered with id:"));
    }

    @Test
    void shouldRegisterNewParcelPost() {
        request.setType("parcelpost");

        response = mailItemService.registerNewMail(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().startsWith("Mail item registered with id:"));
    }

    @Test
    void shouldRegisterNewPostCard() {
        request.setType("postcard");

        response = mailItemService.registerNewMail(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().startsWith("Mail item registered with id:"));
    }

    @Test
    void shouldReturnInvalidType() {
        request.setType("InvalidType");

        response = mailItemService.registerNewMail(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().startsWith("Parcel type only"));
    }

    @Test
    void shouldReturnInternalServerError() {
        request.setType("Mail");

        when(mailItemRepository.save(any())).thenThrow(new RuntimeException());

        response = mailItemService.registerNewMail(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Error registering mail item"));
    }

    @Test
    void shouldValidMailAndPostOfficeForArrive() {
        when(mailItemRepository.findById(anyLong())).thenReturn(Optional.of(mailItem));
        when(postOfficeService.findPostOffice(anyString())).thenReturn(Optional.of(postOffice));

        response = mailItemService.arrive(1L, "123456");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("arrived at postal office"));

        verify(mailStatusService, times(1)).statusChange(any());
    }

    @Test
    void shouldReturnInvalidMail() {
        when(mailItemRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(postOfficeService.findPostOffice(anyString())).thenReturn(Optional.of(new PostOffice()));

        response = mailItemService.arrive(1L, "123456");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(mailStatusService, never()).statusChange(any());
    }

    @Test
    void shouldReturnInvalidPostOffice() {
        when(mailItemRepository.findById(anyLong())).thenReturn(Optional.of(new MailItem()));
        when(postOfficeService.findPostOffice(anyString())).thenReturn(Optional.empty());

        response = mailItemService.arrive(1L, "123456");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(mailStatusService, never()).statusChange(any());
    }

    @Test
    void shouldValidMailAndPostOfficeForDepart() {
        when(mailItemRepository.findById(anyLong())).thenReturn(Optional.of(mailItem));
        when(postOfficeService.findPostOffice(anyString())).thenReturn(Optional.of(postOffice));

        response = mailItemService.depart(1L, "12345");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("departed at postal office"));

        verify(mailStatusService, times(1)).statusChange(any());
    }

    @Test
    public void shouldValidMailAndPostOfficeForReceived() {
        when(mailItemRepository.findById(anyLong())).thenReturn(Optional.of(mailItem));
        when(postOfficeService.findPostOffice(anyString())).thenReturn(Optional.of(postOffice));

        response = mailItemService.received(1L, "123456");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("has been received by the recipient"));

        verify(mailStatusService, times(1)).statusChange(any());
    }
    @Test
    void shouldValidMailItemWithStatus() {
        when(mailItemRepository.findById(anyLong())).thenReturn(Optional.of(mailItem));

        List<MailStatus> statusHistory = new ArrayList<>();
        statusHistory.add(new MailStatus(1L,mailItem, postOffice, "Arrived", LocalDateTime.now()));

        when(mailStatusService.findStatusByMailItem(mailItem)).thenReturn(statusHistory);

        response = mailItemService.getMailStatus(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Current status of mail item with id"));
    }
    @Test
    void shouldReturnEmptyStatus() {
        when(mailItemRepository.findById(anyLong())).thenReturn(Optional.of(mailItem));
        when(mailStatusService.findStatusByMailItem(mailItem)).thenReturn(new ArrayList<>());

        response = mailItemService.getMailStatus(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("No status history available for mail item"));
    }
    @Test
    public void shouldReturnInvalidStatus() {
        when(mailItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        response = mailItemService.getMailStatus(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    public void shouldValidMailItemWithStatusHistory() {
        List<MailStatus> statusByMailItem = new ArrayList<>();
        statusByMailItem.add(new MailStatus(1L,mailItem, postOffice, "Arrived", LocalDateTime.now()));

        when(mailItemRepository.findById(anyLong())).thenReturn(Optional.of(mailItem));
        when(mailStatusService.findStatusByMailItem(mailItem)).thenReturn(statusByMailItem);

        responseListOfMailStatus = mailItemService.getHistory(1L);

        assertEquals(HttpStatus.OK, responseListOfMailStatus.getStatusCode());
        assertFalse(responseListOfMailStatus.getBody().isEmpty());
    }
    @Test
    public void shouldReturnEmptyStatusHistory() {
        when(mailItemRepository.findById(anyLong())).thenReturn(Optional.of(mailItem));
        when(mailStatusService.findStatusByMailItem(mailItem)).thenReturn(new ArrayList<>());

        responseListOfMailStatus = mailItemService.getHistory(1L);

        assertEquals(HttpStatus.OK, responseListOfMailStatus.getStatusCode());
        assertTrue(responseListOfMailStatus.getBody().isEmpty());
    }
    @Test
    public void shouldReturnInvalidStatusHistory() {
        when(mailItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        responseListOfMailStatus = mailItemService.getHistory(1L);

        assertEquals(HttpStatus.NOT_FOUND, responseListOfMailStatus.getStatusCode());
    }
}