package io.ucs.common.base;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import io.ucs.common.constant.CommonConstant;
import io.ucs.sdk.entity.JwtUser;
import io.ucs.util.UcsUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;
import java.util.Optional;

/**
 * @author Macrow
 * @date 2022/07/13
 */
public class AuditMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        boolean createDate = metaObject.hasSetter(metaColumns.getCreatedAt());
        boolean updateDate = metaObject.hasSetter(metaColumns.getUpdatedAt());
        if (createDate || updateDate) {
            Date now = new Date();
            if (createDate) {
                this.setFieldValByName(metaColumns.getCreatedAt(), now, metaObject);
            }
            if (updateDate) {
                this.setFieldValByName(metaColumns.getUpdatedAt(), now, metaObject);
            }
        }
        Optional<JwtUser> optionalJwtUser = UcsUtil.getOptionalJwtUser();
        String username = optionalJwtUser.isPresent() ? optionalJwtUser.get().getName() : "";
        if (metaObject.hasSetter(metaColumns.getCreatedBy())) {
            this.setFieldValByName(metaColumns.getCreatedBy(), username, metaObject);
        }
        if (metaObject.hasSetter(metaColumns.getUpdatedBy())) {
            this.setFieldValByName(metaColumns.getUpdatedBy(), username, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter(metaColumns.getUpdatedAt())) {
            this.setFieldValByName(metaColumns.getUpdatedAt(), new Date(), metaObject);
        }
        if (metaObject.hasSetter(metaColumns.getUpdatedBy())) {
            Optional<JwtUser> optionalJwtUser = UcsUtil.getOptionalJwtUser();
            String username = optionalJwtUser.isPresent() ? optionalJwtUser.get().getName() : "";
            this.setFieldValByName(metaColumns.getUpdatedBy(), username, metaObject);
        }
    }

    private static final MetaColumns metaColumns = initializeMetaColumnNames();

    private static MetaColumns initializeMetaColumnNames() {
        return MetaColumns.builder()
                .createdAt(StrUtil.toCamelCase(CommonConstant.CREATED_AT_META_COLUMN))
                .updatedAt(StrUtil.toCamelCase(CommonConstant.UPDATED_AT_META_COLUMN))
                .createdBy(StrUtil.toCamelCase(CommonConstant.CREATED_BY_META_COLUMN))
                .updatedBy(StrUtil.toCamelCase(CommonConstant.UPDATED_BY_META_COLUMN))
                .build();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    private static class MetaColumns {
        private String createdAt;
        private String updatedAt;
        private String createdBy;
        private String updatedBy;
    }
}