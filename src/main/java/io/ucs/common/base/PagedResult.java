package io.ucs.common.base;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Macrow
 * @date 2022/06/10
 */
@Data
public class PagedResult<T> {
    private Pagination pagination;
    private List<T> items;
    private long total;

    public PagedResult(List<T> items, int page, int pageSize, long totalElement) {
        this.items = items;
        this.total = totalElement;
        this.pagination = Pagination.builder()
                .page(page)
                .pageSize(pageSize)
                .total(totalElement)
                .build();
    }

    public static <T> PagedResult<T> build(List<T> items, int page, int pageSize, long totalElement) {
        return new PagedResult<>(items, page, pageSize, totalElement);
    }

    public static <T> PagedResult<T> build(Page<T> page) {
        return new PagedResult<>(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
    }

    public static <T> PagedResult<T> build(com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page) {
        return new PagedResult<>(page.getRecords(), (int) page.getCurrent(), (int) page.getSize(), page.getTotal());
    }

    @Data
    @Builder
    public static class Pagination {
        private int page;
        private int pageSize;
        private long total;
    }
}
