package io.ucs.common.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author Macrow
 * @date 2022/06/10
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseVo implements Serializable {
    private String id;
}
