package io.ucs.common.base;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import io.ucs.sdk.entity.JwtUser;
import io.ucs.util.UcsUtil;
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
        boolean createDate = metaObject.hasSetter("createdAt");
        boolean updateDate = metaObject.hasSetter("updatedAt");
        if (createDate || updateDate) {
            Date now = new Date();
            if (createDate) {
                this.setFieldValByName("createdAt", now, metaObject);
            }
            if (updateDate) {
                this.setFieldValByName("updatedAt", now, metaObject);
            }
        }
        Optional<JwtUser> optionalJwtUser = UcsUtil.getOptionalJwtUser();
        String username = optionalJwtUser.isPresent() ? optionalJwtUser.get().getName() : "";
        if (metaObject.hasSetter("createdBy")) {
            this.setFieldValByName("createdBy", username, metaObject);
        }
        if (metaObject.hasSetter("updatedBy")) {
            this.setFieldValByName("updatedBy", username, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter("updatedAt")) {
            this.setFieldValByName("updatedAt", new Date(), metaObject);
        }
        if (metaObject.hasSetter("updatedBy")) {
            Optional<JwtUser> optionalJwtUser = UcsUtil.getOptionalJwtUser();
            String username = optionalJwtUser.isPresent() ? optionalJwtUser.get().getName() : "";
            this.setFieldValByName("updatedBy", username, metaObject);
        }
    }
}