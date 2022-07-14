package io.ucs.common.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @date 2022/07/13
 */
@Getter
@Setter
@MappedSuperclass
public abstract class MyBatisAuditingEntity extends MyBatisBaseEntity {
    @TableField(value = CommonConstant.CREATED_AT_META_COLUMN, fill = FieldFill.INSERT)
    protected Date createdAt;

    @TableField(value = CommonConstant.CREATED_BY_META_COLUMN, fill = FieldFill.INSERT)
    protected String createdBy;

    @TableField(value = CommonConstant.UPDATED_AT_META_COLUMN, fill = FieldFill.INSERT_UPDATE)
    protected Date updatedAt;

    @TableField(value = CommonConstant.UPDATED_BY_META_COLUMN, fill = FieldFill.INSERT_UPDATE)
    protected String updatedBy;
}
