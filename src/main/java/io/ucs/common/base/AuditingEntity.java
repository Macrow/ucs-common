package io.ucs.common.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Macrow
 * @date 2022/06/10
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(value = {XIdGeneratorListener.class, AuditingEntityListener.class})
public abstract class AuditingEntity extends BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdAt;

    @LastModifiedBy
    @Column(updatable = false)
    protected String createdBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date updatedAt;

    @LastModifiedBy
    protected String updatedBy;
}
