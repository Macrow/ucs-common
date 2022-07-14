package io.ucs.common.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Macrow
 * @date 2022/06/10
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class AuditingVo implements Serializable {
    private String id;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
}
