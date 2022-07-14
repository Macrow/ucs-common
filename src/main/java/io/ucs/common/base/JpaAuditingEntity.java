package io.ucs.common.base;

import io.ucs.common.constant.CommonConstant;
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
public abstract class JpaAuditingEntity extends JpaBaseEntity {
    @CreatedDate
    @Column(name = CommonConstant.CREATED_AT_META_COLUMN, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdAt;

    @LastModifiedBy
    @Column(name = CommonConstant.CREATED_BY_META_COLUMN, updatable = false)
    protected String createdBy;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = CommonConstant.UPDATED_AT_META_COLUMN)
    protected Date updatedAt;

    @LastModifiedBy
    @Column(name = CommonConstant.UPDATED_BY_META_COLUMN)
    protected String updatedBy;
}
