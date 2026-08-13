package com.example.circle_platform_api.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.circle_platform_api.common.exception.BusinessException;
import com.example.circle_platform_api.common.response.PageResult;
import com.example.circle_platform_api.common.response.ResultCode;
import com.example.circle_platform_api.entity.Category;
import com.example.circle_platform_api.entity.Circle;
import com.example.circle_platform_api.entity.CircleCreateApplication;
import com.example.circle_platform_api.entity.User;
import com.example.circle_platform_api.enums.ApplicationStatus;
import com.example.circle_platform_api.enums.CircleStatus;
import com.example.circle_platform_api.mapstruct.CircleConvert;
import com.example.circle_platform_api.model.dto.CircleCreateApplicationDTO;
import com.example.circle_platform_api.model.dto.CircleQueryDTO;
import com.example.circle_platform_api.model.vo.CircleDetailVo;
import com.example.circle_platform_api.model.vo.CircleVo;
import com.example.circle_platform_api.service.CategoryService;
import com.example.circle_platform_api.service.CircleCreateApplicationService;
import com.example.circle_platform_api.service.CircleService;
import com.example.circle_platform_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CircleApplicationService {

    private final CircleService circleService;
    private final UserService userService;
    private final CircleCreateApplicationService circleCreateApplicationService;
    private final CategoryService categoryService;
    private final CircleConvert circleConvert;

    @Transactional(rollbackFor = Exception.class)
    public void createApplication(CircleCreateApplicationDTO dto) {

        long pendingCount=circleCreateApplicationService.lambdaQuery()
                .eq(CircleCreateApplication::getUserId,dto.getUserId())
                .eq(CircleCreateApplication::getStatus, ApplicationStatus.PENDING)
                .count();

        if (pendingCount>0){
            throw new BusinessException(ResultCode.PENDING_APPLICATION_EXISTS.getCode(),"您有正在审核中的建社申请，请勿重复提交");
        }

        // 创建社团申请
        CircleCreateApplication circleCreateApplication = circleConvert.createToEntity(dto);
        circleCreateApplicationService.save(circleCreateApplication);

    }

    @Transactional(readOnly = true)
    public PageResult<CircleVo> getCircleList(CircleQueryDTO dto) {

        // 构建分页
        Page<Circle> page = Page.of(dto.getPageNum(), dto.getPageSize());

        circleService.lambdaQuery()
                .eq(Circle::getStatus, CircleStatus.NORMAL)
                .page(page);

        if (page.getRecords().isEmpty()) {
            return PageResult.empty(dto.getPageSize());
        }

        List<Long> categoryIds = page.getRecords().stream()
                .map(Circle::getCategoryId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<Long, Category> categoryMap;
        if (!categoryIds.isEmpty()) {
            List<Category> categories = categoryService.lambdaQuery()
                    .in(Category::getId, categoryIds)
                    .list();
            categoryMap = categories.stream()
                    .collect(Collectors.toMap(Category::getId, c -> c));
        } else {
            categoryMap = new HashMap<>();
        }

        // 组装vo
        List<CircleVo> vos = page.getRecords().stream()
                .map(circle -> {
                    Category category = categoryMap.get(circle.getCategoryId());
                    return new CircleVo(
                            circle.getId(),
                            circle.getName(),
                            category != null ? category.getId() : null,
                            category != null ? category.getName() : "未分类",
                            circle.getMemberCount());
                }).toList();

        return PageResult.of(page.getTotal(), page.getCurrent(), page.getSize(), page.getPages(), vos);
    }

    public CircleDetailVo getCircleDetails(Long circleId) {

        Circle circle = circleService.lambdaQuery()
                .eq(Circle::getId, circleId)
                .one();

        if (circle == null) {
            throw new BusinessException(ResultCode.CIRCLE_NOT_FOUND);
        }

        if (circle.getStatus() == CircleStatus.DISSOLVED) {
            throw new BusinessException(ResultCode.CIRCLE_DISSOLVED);
        }

        User user = userService.getById(circle.getOwnerId());
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        String ownerName = StringUtils.hasText(user.getNickname()) ? user.getNickname() : user.getUsername();

        return new CircleDetailVo(
                circle.getId(),
                circle.getName(),
                circle.getDescription(),
                circle.getOwnerId(),
                ownerName,
                circle.getMemberCount()
        );
    }


}
