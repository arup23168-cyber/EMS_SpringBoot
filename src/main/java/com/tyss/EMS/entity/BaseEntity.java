package com.tyss.EMS.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedBy
    @Column(
            name = "created_by",
            updatable = false,
            columnDefinition = "VARCHAR(50) DEFAULT 'SYSTEM'"
    )
    private String createdBy;

    @CreatedDate
    @Column(
            name = "created_at",
            updatable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(
            name = "updated_by",
            columnDefinition = "VARCHAR(50) DEFAULT 'SYSTEM'"
    )
    private String updatedBy;

    @LastModifiedDate
    @Column(
            name = "updated_at",
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
    )
    private LocalDateTime updatedAt;
}