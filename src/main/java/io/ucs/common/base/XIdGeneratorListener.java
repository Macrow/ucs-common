package io.ucs.common.base;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import com.github.shamil.Xid;
import io.ucs.common.annotation.XId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.PrePersist;
import java.lang.reflect.Field;

/**
 * @author Macrow
 * @date 2022/06/10
 */
@Slf4j
@Configurable
public class XIdGeneratorListener {
    @PrePersist
    public void prePersist(Object target) {
        Assert.notNull(target);
        Field[] fields = ReflectUtil.getFields(target.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getDeclaredAnnotation(XId.class) != null) {
                ReflectUtil.setFieldValue(target, "id", Xid.get().toString());
            }
        }
    }
}
