package com.example.circle_platform_api.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.circle_platform_api.common.response.PageResult;

import java.util.List;
import java.util.function.Function;

public class PageUtil {

    public static <T, R> PageResult<R> build(Page<T> page, Function<T, R> structMapper) {
        if (page == null) return PageResult.empty();

        List<R> records = page.getRecords() != null
                ? page.getRecords().stream().map(structMapper).toList()
                : List.of();

        return PageResult.of(
                page.getTotal(),
                page.getCurrent(),
                page.getSize(),
                page.getPages(),
                records
        );
    }

}
