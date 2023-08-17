package com.example.postoffice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table
public class MailStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mail_item_id")
    private MailItem mailItem;

    @ManyToOne
    @JoinColumn(name = "post_office_id")
    private PostOffice postOffice;

    private String status;
    private LocalDateTime timestamp;
}
