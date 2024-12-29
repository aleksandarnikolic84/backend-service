package com.incode.backendservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.OffsetDateTime;

@Entity
@Data
@Table(name = "verification",
        indexes = {@Index(name = "verification__verification_id_IDX", columnList = "verification_id")})
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verification_id_seq")
    @SequenceGenerator(name = "verification_id_seq", sequenceName = "verification_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "verification_id", nullable = false)
    private String verificationId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "query_text", columnDefinition = "jsonb")
    private QueryParams queryText;

    @Column(name = "timestamp", nullable = false)
    private OffsetDateTime timestamp;

    @Column(name = "source")
    private String source;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "result", columnDefinition = "jsonb")
    private String result;

    @CreatedDate
    @Column(name = "created_time", nullable = false, updatable = false)
    private OffsetDateTime createdTime;

    @LastModifiedDate
    @Column(name = "updated_time")
    private OffsetDateTime updatedTime;

}
