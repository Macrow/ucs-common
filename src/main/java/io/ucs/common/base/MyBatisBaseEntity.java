package io.ucs.common.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.ucs.common.annotation.XId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * @author Macrow
 * @date 2022/07/13
 */
@Getter
@Setter
@MappedSuperclass
public abstract class MyBatisBaseEntity implements Serializable {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
}
