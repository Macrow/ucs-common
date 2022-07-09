package io.ucs.common.base;

import lombok.*;

import java.io.Serializable;

/**
 * @author Macrow
 * @date 2022/06/10
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RawResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private T result;
}
