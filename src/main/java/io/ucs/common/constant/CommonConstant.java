package io.ucs.common.constant;

/**
 * @author Macrow
 * @date 2022/06/10
 */
public interface CommonConstant {
    Integer AUTO_EXPIRE_DAYS = 30;
    /**
     * 缓存空间分隔符
     */
    String CACHE_SPLITTER = ":";

    /**
     * 通用数组组合的分隔符
     */
    String COMMON_SPLITTER = ",";

    /**
     * 特殊分隔符
     */
    String SHARP_SPLITTER = "#";
    String AT_SPLITTER = "@";
    String LINE_SPLITTER = "-";

    String MY_BATIS_PLUS_AUDIT_HANDLER = "auditMetaObjectHandler";
}
