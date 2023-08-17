package com.example.postoffice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "mail_item_id")
    private MailItem mailItem;

    @ManyToOne
    @JoinColumn(name = "post_office_id")
    private PostOffice postOffice;

    private String status;
    private LocalDateTime timestamp;
}
