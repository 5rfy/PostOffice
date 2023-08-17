package com.example.postoffice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table
public class MailItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "mailItem")
    private List<MailStatus> mailHistory = new ArrayList<>();

    private String type;
    private String recipientIndex;
    private String recipientAddress;
    private String recipientName;
}
