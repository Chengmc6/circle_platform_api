package com.example.circle_platform_api.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<R> {
    private Long total;
    private Long currentPage;
    private Long pageSize;
    private Long totalPages;
    private List<R> records;

    /**
     * 构建分页结果（推荐使用）
     */
    public static <R> PageResult<R> of(Long total, Long currentPage, Long pageSize, Long totalPages, List<R> records) {
        if (total == null || total < 0) total = 0L;
        if (currentPage == null || currentPage < 1) currentPage = 1L;
        if (pageSize == null || pageSize < 1) pageSize = 10L;
        if (totalPages == null) totalPages = (long) Math.ceil((double) total / pageSize);

        return new PageResult<>(total, currentPage, pageSize, totalPages, records);
    }

    /**
     * 返回空分页结果
     */
    public static <R> PageResult<R> empty() {
        return of(0L, 1L, 10L, 0L, List.of());
    }

    /**
     * 返回空分页结果（保持请求的 pageSize）
     */
    public static <R> PageResult<R> empty(Long currentPage,Long pageSize) {
        return of(0L, currentPage, pageSize != null && pageSize >= 1 ? pageSize : 10L, 0L, List.of());
    }
}
