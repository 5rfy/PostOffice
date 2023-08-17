package com.example.postoffice.controllers;

import com.example.postoffice.dto.MailItemRequest;
import com.example.postoffice.dto.MailStatusResponse;
import com.example.postoffice.service.MailItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailItemService mailItemService;

    @PostMapping("/register")
    public ResponseEntity<String> registerMailItem(@RequestBody MailItemRequest mailItemRequest) {
        return mailItemService.registerNewMail(mailItemRequest);
    }
    @PostMapping("/arrive")
    public ResponseEntity<String> arriveAtPostalOffice(@RequestParam Long mailId,
                                                       @RequestParam String postIndex) {
        return mailItemService.arrive(mailId, postIndex);
    }
    @PostMapping("/depart")
    public ResponseEntity<String> departFromPostalOffice(@RequestParam Long mailId,
                                                         @RequestParam String postIndex) {
        return mailItemService.depart(mailId, postIndex);
    }
    @PostMapping("/receive")
    public ResponseEntity<String> receiveFromPostalOffice(@RequestParam Long mailId,
                                                         @RequestParam String postIndex) {

        return mailItemService.received(mailId,postIndex);
    }
    @GetMapping("/status")
    public ResponseEntity<String> getMailItemStatus(@RequestParam Long mailId) {

        return mailItemService.getMailStatus(mailId);
    }
    @GetMapping("/history")
    public ResponseEntity<List<MailStatusResponse>> getMailItemHistory(@RequestParam Long mailId) {

        return mailItemService.getHistory(mailId);
    }

}
