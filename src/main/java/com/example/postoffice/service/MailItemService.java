package com.example.postoffice.service;

import com.example.postoffice.dto.MailItemRequest;
import com.example.postoffice.dto.MailItemResponse;
import com.example.postoffice.dto.MailStatusResponse;
import com.example.postoffice.dto.PostOfficeResponse;
import com.example.postoffice.model.MailItem;
import com.example.postoffice.model.MailStatus;
import com.example.postoffice.model.PostOffice;
import com.example.postoffice.repository.MailItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MailItemService {
    private final MailItemRepository mailItemRepository;
    private final PostOfficeService postOfficeService;
    private final MailStatusService mailStatusService;

    private final static String[] typeMail = {"mail", "box", "parcelpost", "postcard"};
    @Transactional
    public ResponseEntity<String> registerNewMail(MailItemRequest mailItemRequest) {
        MailItem newMailItem = MailItem.builder()
                .type(mailItemRequest.getType())
                .recipientIndex(mailItemRequest.getRecipientIndex())
                .recipientAddress(mailItemRequest.getRecipientAddress())
                .recipientName(mailItemRequest.getRecipientName())
                .build();
        try {
            Arrays.stream(typeMail)
                    .filter(type -> type.contains(newMailItem.getType().toLowerCase()))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);

            mailItemRepository.save(newMailItem);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Mail item registered with id: " + newMailItem.getId());
        }catch (IllegalArgumentException illegalArgumentException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Parcel type only: Mail, Box, ParcelPost, Postcard");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error registering mail item: " + newMailItem.getId());
        }
    }

    @Transactional
    public ResponseEntity<String> arrive(Long mailId, String postIndex) {
        Optional<MailItem> getMailItemById = mailItemRepository.findById(mailId);
        Optional<PostOffice> getPostOfficeByIndex = postOfficeService.findPostOffice(postIndex);

        if (getMailItemById.isPresent() && getPostOfficeByIndex.isPresent()) {
            MailItem mailItem = getMailItemById.get();
            PostOffice postOffice = getPostOfficeByIndex.get();

            MailStatus arriveStatus = MailStatus.builder()
                    .postOffice(postOffice)
                    .mailItem(mailItem)
                    .status("Arrived")
                    .timestamp(LocalDateTime.now())
                    .build();

            mailStatusService.statusChange(arriveStatus);

            return ResponseEntity
                    .ok("Mail item with id " + mailId + " arrived at postal office " + postIndex);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @Transactional
    public ResponseEntity<String> depart(Long mailId, String postIndex) {
        Optional<MailItem> getMailItemById = mailItemRepository.findById(mailId);
        Optional<PostOffice> getPostOfficeByIndex = postOfficeService.findPostOffice(postIndex);

        if (getMailItemById.isPresent() && getPostOfficeByIndex.isPresent()) {
            MailItem mailItem = getMailItemById.get();
            PostOffice postOffice = getPostOfficeByIndex.get();

            MailStatus departureStatus = MailStatus.builder()
                    .postOffice(postOffice)
                    .mailItem(mailItem)
                    .status("Departed")
                    .timestamp(LocalDateTime.now())
                    .build();

            mailStatusService.statusChange(departureStatus);

            return ResponseEntity
                    .ok("Mail item with id " + mailId + " departed at postal office " + postIndex);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @Transactional
    public ResponseEntity<String> received(Long mailId, String postIndex) {
        Optional<MailItem> getMailItemById = mailItemRepository.findById(mailId);
        Optional<PostOffice> getPostOfficeByIndex = postOfficeService.findPostOffice(postIndex);

        if (getMailItemById.isPresent() && getPostOfficeByIndex.isPresent()) {
            MailItem mailItem = getMailItemById.get();
            PostOffice postOffice = getPostOfficeByIndex.get();

            MailStatus receivedStatus = MailStatus.builder()
                    .postOffice(postOffice)
                    .mailItem(mailItem)
                    .status("Received")
                    .timestamp(LocalDateTime.now())
                    .build();

            mailStatusService.statusChange(receivedStatus);

            return ResponseEntity.ok("Mail item with id " + mailId + " has been received by the recipient");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<String> getMailStatus(Long mailId) {
        Optional<MailItem> getMailItemById = mailItemRepository.findById(mailId);

        if (getMailItemById.isPresent()) {
            MailItem mailItem = getMailItemById.get();

            List<MailStatus> statusHistory = mailStatusService.findStatusByMailItem(mailItem);

            if (!statusHistory.isEmpty()) {
                MailStatus currentStatus = statusHistory.get(statusHistory.size()-1);
                return ResponseEntity
                        .ok("Current status of mail item with id " + mailId + ": " + currentStatus.getStatus());
            } else {
                return ResponseEntity.ok("No status history available for mail item with id " + mailId);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<MailStatusResponse>> getHistory(Long mailId) {
        Optional<MailItem> getMailItemById = mailItemRepository.findById(mailId);

        if (getMailItemById.isPresent()) {
            MailItem mailItem = getMailItemById.get();

            List<MailStatus> statusByMailItem = mailStatusService.findStatusByMailItem(mailItem);

            List<MailStatusResponse> statusHistory =
                    statusByMailItem.stream()
                            .map(this::mapToMailStatusResponse)
                            .collect(Collectors.toList());

            return ResponseEntity.ok(statusHistory);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private MailStatusResponse mapToMailStatusResponse(MailStatus mailStatus) {
        return MailStatusResponse.builder()
                .postOfficeResponse(mapPostOffice(mailStatus.getPostOffice()))
                .mailItemResponse(mapMailItem(mailStatus.getMailItem()))
                .status(mailStatus.getStatus())
                .timestamp(mailStatus.getTimestamp())
                .build();
    }

    private MailItemResponse mapMailItem(MailItem mailItem) {
        return MailItemResponse.builder()
                .type(mailItem.getType())
                .recipientIndex(mailItem.getRecipientIndex())
                .recipientAddress(mailItem.getRecipientAddress())
                .recipientName(mailItem.getRecipientName())
                .build();
    }

    private PostOfficeResponse mapPostOffice(PostOffice postOffice) {
        return PostOfficeResponse.builder()
                .index(postOffice.getIndex())
                .address(postOffice.getAddress())
                .name(postOffice.getName())
                .build();
    }
}

