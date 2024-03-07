package com.gng.ash.fileconverter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private int httpResponseCode;
    private String uri;
    private String ip;
    private String countryCode;
    private String isp;

    private Date requestTime;
    private long elapsedTime;
}
