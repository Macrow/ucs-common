package io.ucs.common.base;

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
 * @date 2022/06/10
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(value = {XIdGeneratorListener.class})
public abstract class JpaBaseEntity implements Serializable {
    @Id
    @XId
    @Column(length = 20)
    protected String id;
}
