package io.ucs.common.base;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author Macrow
 * @date 2022/06/10
 */
@FunctionalInterface
public interface SFunction<T, R> extends Function<T, R>, Serializable {
}