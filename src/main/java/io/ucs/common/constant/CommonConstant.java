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

    String ID_META_COLUMN = "id";
    String CREATED_AT_META_COLUMN = "created_at";
    String CREATED_BY_META_COLUMN = "created_by";
    String UPDATED_AT_META_COLUMN = "updated_at";
    String UPDATED_BY_META_COLUMN = "updated_by";
    String MY_BATIS_PLUS_AUDIT_HANDLER = "auditMetaObjectHandler";
}
