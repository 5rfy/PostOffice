package com.example.postoffice.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MailStatusResponse {

    private MailItemResponse mailItemResponse;
    private PostOfficeResponse postOfficeResponse;
    private String status;
    private LocalDateTime timestamp;
}
