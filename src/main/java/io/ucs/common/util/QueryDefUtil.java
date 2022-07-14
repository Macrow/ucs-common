package io.ucs.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.ucs.common.annotation.QueryDef;
import io.ucs.common.base.AntDesignPageQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Macrow
 * @date 2022/06/10
 */
@Slf4j
@SuppressWarnings({"unchecked", "all"})
public class QueryDefUtil {
    private static final String STRING_JOINER_SPLITTER = ",";

    public static <R, Q> Predicate getPredicate(Root<R> root, Q query, CriteriaBuilder cb) {
        List<Predicate> list = new ArrayList<>();
        if (query == null) {
            return cb.and(list.toArray(new Predicate[0]));
        }
        try {
            List<Field> fields = getAllFields(query.getClass(), new ArrayList<>());
            for (Field field : fields) {
                // 设置对象的访问权限，保证对private的属性的访问权限
                field.setAccessible(true);
                QueryDef q = field.getAnnotation(QueryDef.class);
                if (q != null) {
                    String propName = q.propName();
                    String joinName = q.joinName();
                    String blurry = q.blurry();
                    String attributeName = isBlank(propName) ? field.getName() : propName;
                    Class<?> fieldType = field.getType();
                    Object val = field.get(query);
                    if (ObjectUtil.isNull(val) || "".equals(val)) {
                        continue;
                    }
                    Join join = null;
                    // 模糊多字段
                    if (ObjectUtil.isNotEmpty(blurry)) {
                        String[] blurrys = blurry.split(STRING_JOINER_SPLITTER);
                        List<Predicate> orPredicate = new ArrayList<>();
                        for (String s : blurrys) {
                            orPredicate.add(cb.like(root.get(s)
                                    .as(String.class), "%" + val.toString() + "%"));
                        }
                        Predicate[] p = new Predicate[orPredicate.size()];
                        list.add(cb.or(orPredicate.toArray(p)));
                        continue;
                    }
                    if (ObjectUtil.isNotEmpty(joinName)) {
                        String[] joinNames = joinName.split(">");
                        for (String name : joinNames) {
                            JoinType joinType = JoinType.LEFT;
                            switch (q.join()) {
                                case LEFT:
                                    joinType = JoinType.LEFT;
                                    break;
                                case RIGHT:
                                    joinType = JoinType.RIGHT;
                                    break;
                                case INNER:
                                    joinType = JoinType.INNER;
                                    break;
                                default:
                                    break;
                            }
                            if (ObjectUtil.isNotNull(join) && ObjectUtil.isNotNull(val)) {
                                join = join.join(name, joinType);
                            }
                        }
                    }
                    switch (q.type()) {
                        case EQUAL:
                            list.add(cb.equal(getExpression(attributeName, join, root)
                                    .as((Class<? extends Comparable>) fieldType), val));
                            break;
                        case GREATER_THAN:
                            list.add(cb.greaterThan(getExpression(attributeName, join, root)
                                    .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                            break;
                        case LESS_THAN:
                            list.add(cb.lessThan(getExpression(attributeName, join, root)
                                    .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                            break;
                        case LESS_THAN_OR_EQUAL_TO:
                            list.add(cb.lessThanOrEqualTo(getExpression(attributeName, join, root)
                                    .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                            break;
                        case GREATER_THAN_OR_EQUAL_TO:
                            list.add(cb.greaterThanOrEqualTo(getExpression(attributeName, join, root)
                                    .as((Class<? extends Comparable>) fieldType), (Comparable) val));
                            break;
                        case INNER_LIKE:
                            list.add(cb.like(getExpression(attributeName, join, root)
                                    .as(String.class), "%" + val.toString() + "%"));
                            break;
                        case LEFT_LIKE:
                            list.add(cb.like(getExpression(attributeName, join, root)
                                    .as(String.class), "%" + val.toString()));
                            break;
                        case RIGHT_LIKE:
                            list.add(cb.like(getExpression(attributeName, join, root)
                                    .as(String.class), val.toString() + "%"));
                            break;
                        case INNER_LIKE_IGNORE_CASE:
                            list.add(cb.like(cb.lower(getExpression(attributeName, join, root)
                                    .as(String.class)), "%" + val.toString().toLowerCase() + "%"));
                            break;
                        case LEFT_LIKE_IGNORE_CASE:
                            list.add(cb.like(cb.lower(getExpression(attributeName, join, root)
                                    .as(String.class)), "%" + val.toString().toLowerCase()));
                            break;
                        case RIGHT_LIKE_IGNORE_CASE:
                            list.add(cb.like(cb.lower(getExpression(attributeName, join, root)
                                    .as(String.class)), val.toString().toLowerCase() + "%"));
                            break;
                        case IN:
                            // 用逗号分隔的字符串，转换为数组
                            if (q.inListString()) {
                                val = Arrays.stream(((String) val).split(STRING_JOINER_SPLITTER)).map(String::trim).collect(Collectors.toSet());
                            }
                            if (CollUtil.isNotEmpty((Collection<Long>) val)) {
                                list.add(getExpression(attributeName, join, root).in((Collection<Long>) val));
                            }
                            break;
                        case NOT_EQUAL:
                            list.add(cb.notEqual(getExpression(attributeName, join, root), val));
                            break;
                        case NOT_NULL:
                            if (Boolean.class == val.getClass() && (Boolean) val == true) {
                                list.add(cb.isNotNull(getExpression(attributeName, join, root)));
                            }
                            break;
                        case IS_NULL:
                            if (Boolean.class == val.getClass() && (Boolean) val == true) {
                                list.add(cb.isNull(getExpression(attributeName, join, root)));
                            }
                            break;
                        case BETWEEN:
                            List<Object> between = new ArrayList<>((List<Object>) val);
                            list.add(cb.between(getExpression(attributeName, join, root).as((Class<? extends Comparable>) between.get(0).getClass()),
                                    (Comparable) between.get(0), (Comparable) between.get(1)));
                            break;
                        case START_DATE:
                        case END_DATE:
                            DateTime dateTime = DateUtil.parse(StrUtil.toString(val));
                            String dateString = DateUtil.format(dateTime, "yyyy-MM-dd");
                            if (q.type().equals(QueryDef.Type.START_DATE)) {
                                Date date = DateUtil.beginOfDay(DateUtil.parse(dateString, "yyyy-MM-dd"));
                                list.add(cb.greaterThanOrEqualTo(getExpression(attributeName, join, root).as(Date.class), date));
                            }
                            if (q.type().equals(QueryDef.Type.END_DATE)) {
                                Date date = DateUtil.endOfDay(DateUtil.parse(dateString, "yyyy-MM-dd"));
                                list.add(cb.lessThanOrEqualTo(getExpression(attributeName, join, root).as(Date.class), date));
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        int size = list.size();
        return cb.and(list.toArray(new Predicate[size]));
    }

    public static <T, Q> QueryWrapper getQueryWrapper(Q query) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (query == null) {
            return queryWrapper;
        }
        try {
            List<Field> fields = getAllFields(query.getClass(), new ArrayList<>());
            for (Field field : fields) {
                // 设置对象的访问权限，保证对private的属性的访问权限
                field.setAccessible(true);
                QueryDef q = field.getAnnotation(QueryDef.class);
                if (q != null) {
                    String propName = q.propName();
                    String blurry = q.blurry();
                    String attributeName = isBlank(propName) ? field.getName() : propName;
                    Class<?> fieldType = field.getType();
                    final Object val = field.get(query);
                    if (ObjectUtil.isNull(val) || "".equals(val)) {
                        continue;
                    }
                    // 模糊多字段
                    if (ObjectUtil.isNotEmpty(blurry)) {
                        String[] blurrys = blurry.split(STRING_JOINER_SPLITTER);
                        queryWrapper.and(qw -> {
                            for (int i = 0; i < blurrys.length; i++) {
                                if (i > 0) {
                                    qw.or();
                                }
                                qw.like(blurrys[i], val.toString());
                            }
                        });
                        continue;
                    }
                    switch (q.type()) {
                        case EQUAL:
                            queryWrapper.and(qw -> qw.eq(attributeName, val));
                            break;
                        case GREATER_THAN:
                            queryWrapper.and(qw -> qw.gt(attributeName, (Comparable) val));
                            break;
                        case LESS_THAN:
                            queryWrapper.and(qw -> qw.lt(attributeName, (Comparable) val));
                            break;
                        case LESS_THAN_OR_EQUAL_TO:
                            queryWrapper.and(qw -> qw.le(attributeName, (Comparable) val));
                            break;
                        case GREATER_THAN_OR_EQUAL_TO:
                            queryWrapper.and(qw -> qw.ge(attributeName, (Comparable) val));
                            break;
                        case INNER_LIKE:
                            queryWrapper.and(qw -> qw.like(attributeName, val.toString()));
                            break;
                        case INNER_LIKE_IGNORE_CASE:
                            queryWrapper.and(qw -> qw.like("LOWER(" + attributeName + ")", val.toString().toLowerCase()));
                            break;
                        case LEFT_LIKE:
                            queryWrapper.and(qw -> qw.likeLeft(attributeName, val.toString()));
                            break;
                        case LEFT_LIKE_IGNORE_CASE:
                            queryWrapper.and(qw -> qw.likeLeft("LOWER(" + attributeName + ")", val.toString().toLowerCase()));
                            break;
                        case RIGHT_LIKE:
                            queryWrapper.and(qw -> qw.likeRight(attributeName, val.toString()));
                            break;
                        case RIGHT_LIKE_IGNORE_CASE:
                            queryWrapper.and(qw -> qw.likeRight("LOWER(" + attributeName + ")", val.toString().toLowerCase()));
                            break;
                        case IN:
                            // 用逗号分隔的字符串，转换为数组
                            if (q.inListString()) {
                                final Set<String> inCollection = Arrays.stream(((String) val).split(STRING_JOINER_SPLITTER)).map(String::trim).collect(Collectors.toSet());
                                if (CollUtil.isNotEmpty(inCollection)) {
                                    queryWrapper.and(qw -> qw.in(attributeName, inCollection));
                                }
                            }
                            break;
                        case NOT_EQUAL:
                            queryWrapper.and(qw -> qw.ne(attributeName, val));
                            break;
                        case NOT_NULL:
                            if (Boolean.class == val.getClass() && (Boolean) val == true) {
                                queryWrapper.and(qw -> qw.isNotNull(attributeName));
                            }
                            break;
                        case IS_NULL:
                            if (Boolean.class == val.getClass() && (Boolean) val == true) {
                                queryWrapper.and(qw -> qw.isNull(attributeName));
                            }
                            break;
                        case BETWEEN:
                            List<Object> between = new ArrayList<>((List<Object>) val);
                            queryWrapper.and(qw -> qw.between(attributeName, (Comparable) between.get(0), (Comparable) between.get(1)));
                            break;
                        case START_DATE:
                        case END_DATE:
                            DateTime dateTime = DateUtil.parse(StrUtil.toString(val));
                            String dateString = DateUtil.format(dateTime, "yyyy-MM-dd");
                            if (q.type().equals(QueryDef.Type.START_DATE)) {
                                Date date = DateUtil.beginOfDay(DateUtil.parse(dateString, "yyyy-MM-dd"));
                                queryWrapper.and(qw -> qw.ge(attributeName, date));
                            }
                            if (q.type().equals(QueryDef.Type.END_DATE)) {
                                Date date = DateUtil.endOfDay(DateUtil.parse(dateString, "yyyy-MM-dd"));
                                queryWrapper.and(qw -> qw.le(attributeName, date));
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return queryWrapper;
    }

    public static <T, Q> QueryWrapper getQueryWrapper(Q query, AntDesignPageQuery antDesignPageQuery) {
        QueryWrapper queryWrapper = getQueryWrapper(query);
        if (StrUtil.isNotEmpty(antDesignPageQuery.getField())) {
            if (StrUtil.isEmpty(antDesignPageQuery.getOrder())) {
                antDesignPageQuery.setOrder(AntDesignPageQuery.ASC);
            }
            switch (antDesignPageQuery.getOrder().toLowerCase()) {
                case AntDesignPageQuery.DESC:
                    queryWrapper.orderByDesc(antDesignPageQuery.getField());
                    break;
                case AntDesignPageQuery.ASC:
                default:
                    queryWrapper.orderByAsc(antDesignPageQuery.getField());
                    break;
            }
        }
        return queryWrapper;
    }

    private static <T, R> Expression<T> getExpression(String attributeName, Join join, Root<R> root) {
        if (ObjectUtil.isNotEmpty(join)) {
            return join.get(attributeName);
        } else {
            return root.get(attributeName);
        }
    }

    private static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static List<Field> getAllFields(Class clazz, List<Field> fields) {
        if (clazz != null) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            getAllFields(clazz.getSuperclass(), fields);
        }
        return fields;
    }

    public static <T, R> Page<R> myBatisPlusMap(Page<T> page, Function<T, R> converter) {
        List<R> records = page.getRecords().stream().map(r -> converter.apply(r)).collect(Collectors.toList());
        return new Page<R>(page.getCurrent(), page.getSize(), page.getTotal(), page.searchCount()).setRecords(records);
    }
}
