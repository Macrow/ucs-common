package io.ucs.common.base;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Macrow
 * @date 2022/06/10
 * @desc 针对Ant Design进行分页排序查询
 */
public class AntDesignPageQuery implements Serializable {
    private static final String ASC = "ascend";
    private static final String DESC = "descend";

    private static final Integer PAGE_SIZE_DEFAULT = 10;
    private static final Integer CURRENT_DEFAULT = 1;
    private static final String DIRECTION_DEFAULT = ASC;

    @Setter private Integer pageSize;
    @Setter private Integer page;
    @Setter @Getter private String field;
    @Setter @Getter private String order;

    private String sortDefault = null;
    private Sort.Direction directionDefault = null;
    private Map<String, Sort.Direction> otherSortMap = null;

    public Integer getPageSize() {
        if (this.pageSize == null) {
            return PAGE_SIZE_DEFAULT;
        }
        return this.pageSize;
    }

    public Integer getPage() {
        if (this.page == null) {
            return CURRENT_DEFAULT;
        }
        return this.page;
    }

    public Pageable pageable(String columnName, Sort.Direction directionDefault) {
        this.sortDefault = columnName;
        this.directionDefault = directionDefault;
        return pageable();
    }

    public <T> Pageable pageable(SFunction<T, ?> columnFn, Sort.Direction directionDefault) {
        // 从function取出序列化方法
        Method writeReplaceMethod;
        try {
            writeReplaceMethod = columnFn.getClass().getDeclaredMethod("writeReplace");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // 从序列化方法取出序列化的lambda信息
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(columnFn);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        // 从lambda信息取出method、field、class等
        String fieldName = serializedLambda.getImplMethodName().substring("get".length());
        fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());

        return pageable(fieldName, directionDefault);
    }

    public <T> AntDesignPageQuery addSort(SFunction<T, ?> columnFn, Sort.Direction sort) {
        // 从function取出序列化方法
        Method writeReplaceMethod;
        try {
            writeReplaceMethod = columnFn.getClass().getDeclaredMethod("writeReplace");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // 从序列化方法取出序列化的lambda信息
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(columnFn);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        // 从lambda信息取出method、field、class等
        String fieldName = serializedLambda.getImplMethodName().substring("get".length());
        fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());

        if (this.otherSortMap == null) {
            this.otherSortMap = new HashMap<>();
        }
        this.otherSortMap.put(fieldName, sort);
        return this;
    }

    public Pageable pageable() {
        initializeByDefaultValue();
        Sort.Direction directionType;
        switch (order.toLowerCase()) {
            case DESC:
                directionType = Sort.Direction.DESC;
                break;
            case ASC:
            default:
                directionType = Sort.Direction.ASC;
                break;
        }
        List<Sort.Order> sortList = new ArrayList<>();
        if (!StrUtil.isBlank(field)) {
            sortList.add(new Sort.Order(directionType, field));
        }
        if (otherSortMap != null) {
            otherSortMap.forEach((k, v) -> sortList.add(new Sort.Order(v, k)));
        }
        if (sortDefault != null && directionDefault != null && sortList.isEmpty()) {
            sortList.add(new Sort.Order(directionDefault, sortDefault));
        }
        return PageRequest.of(page - 1, pageSize, Sort.by(sortList));
    }

    private void initializeByDefaultValue() {
        if (pageSize == null) {
            pageSize = PAGE_SIZE_DEFAULT;
        }
        if (page == null) {
            page = CURRENT_DEFAULT;
        }
        if (order == null) {
            order = DIRECTION_DEFAULT;
        }
    }
}
