package com.application.apigateway.entity;

import com.application.apigateway.enums.CommonEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class CommonAttributes {
    @CreationTimestamp
    private LocalDateTime createdDT;
    @LastModifiedDate
    private LocalDateTime updatedDT;
    private LocalDateTime deletedDT;
    private String createdByUsername;
    private String updatedByUsername;
    private String deletedByUsername;
    private Long createdBy;
    private Long updatedBy;
    private Long deletedBy;

    @Enumerated(EnumType.STRING)
    private CommonEnum status = CommonEnum.Active;
}
