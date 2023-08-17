package com.example.postoffice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MailItemResponse {
    private String type;
    private String recipientIndex;
    private String recipientAddress;
    private String recipientName;
}
