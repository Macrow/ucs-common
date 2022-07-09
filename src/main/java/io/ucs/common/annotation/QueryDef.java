package io.ucs.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Macrow
 * @date 2022/06/10
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryDef {

    String propName() default "";

    Type type() default Type.EQUAL;

    /**
     * 数组查询使用的用逗号分隔的字符串，在查询时转换为集合
     */
    boolean inListString() default false;

    /**
     * 连接查询的属性名，如User类中的dept
     */
    String joinName() default "";

    /**
     * 默认左连接
     */
    Join join() default Join.LEFT;

    /**
     * 多字段模糊搜索，仅支持String类型字段，多个用逗号隔开, 如@Query(blurry = "email,username")
     */
    String blurry() default "";

    enum Type {
        // 相等
        EQUAL,
        // 大于等于
        GREATER_THAN_OR_EQUAL_TO,
        // 小于等于
        LESS_THAN_OR_EQUAL_TO,
        // 大于
        GREATER_THAN,
        // 小于
        LESS_THAN,
        // 中模糊查询 %param%
        INNER_LIKE,
        // 左模糊查询 %param
        LEFT_LIKE,
        // 右模糊查询 param%
        RIGHT_LIKE,
        // 中模糊查询[忽略大小写] %param%
        INNER_LIKE_IGNORE_CASE,
        // 左模糊查询[忽略大小写] %param
        LEFT_LIKE_IGNORE_CASE,
        // 右模糊查询[忽略大小写] param%
        RIGHT_LIKE_IGNORE_CASE,
        // 包含
        IN,
        // 不等于
        NOT_EQUAL,
        // between
        BETWEEN,
        // 不为空
        NOT_NULL,
        // 为空
        IS_NULL,
        // 作为开始时间进行比较
        START_DATE,
        // 作为结束时间进行比较
        END_DATE,
    }

    /**
     * 适用于简单连接查询，复杂的请自定义该注解，或者使用sql查询
     */
    enum Join {
        // left join
        LEFT,
        // right join
        RIGHT,
        /// inner join
        INNER
    }

}
