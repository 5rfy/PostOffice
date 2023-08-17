package com.example.postoffice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MailItemRequest {
    private String type;
    private String recipientIndex;
    private String recipientAddress;
    private String recipientName;
}
