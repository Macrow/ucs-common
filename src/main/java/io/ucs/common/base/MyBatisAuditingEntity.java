package io.ucs.common.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @date 2022/07/13
 */
@Getter
@Setter
@MappedSuperclass
public abstract class MyBatisAuditingEntity extends MyBatisBaseEntity {
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    protected Date createdAt;

    @TableField(value = "created_by", fill = FieldFill.INSERT)
    protected String createdBy;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    protected Date updatedAt;

    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    protected String updatedBy;
}
