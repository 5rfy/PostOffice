package com.example.postoffice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PostOfficeResponse {
    private String index;
    private String name;
    private String address;
}
