package io.ucs.common.base;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.github.shamil.Xid;
import org.springframework.stereotype.Component;

/**
 * @author Macrow
 * @date 2022/07/13
 */
@Component
public class XidGenerator implements IdentifierGenerator {
    @Override
    public Number nextId(Object entity) {
        return IdUtil.getSnowflakeNextId();
    }

    @Override
    public String nextUUID(Object entity) {
        return Xid.get().toString();
    }
}
