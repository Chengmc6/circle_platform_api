package com.example.circle_platform_api.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.circle_platform_api.common.exception.BusinessException;
import com.example.circle_platform_api.common.response.PageResult;
import com.example.circle_platform_api.common.response.ResultCode;
import com.example.circle_platform_api.entity.Circle;
import com.example.circle_platform_api.entity.CircleCreateApplication;
import com.example.circle_platform_api.entity.User;
import com.example.circle_platform_api.enums.ApplicationStatus;
import com.example.circle_platform_api.enums.CircleStatus;
import com.example.circle_platform_api.enums.UserRole;
import com.example.circle_platform_api.mapstruct.CircleConvert;
import com.example.circle_platform_api.model.dto.*;
import com.example.circle_platform_api.model.vo.CircleCreateApplicationVo;
import com.example.circle_platform_api.model.vo.CircleDetailVo;
import com.example.circle_platform_api.model.vo.CircleVo;
import com.example.circle_platform_api.security.service.SecurityUser;
import com.example.circle_platform_api.security.utils.SecurityUtils;
import com.example.circle_platform_api.service.CircleCreateApplicationService;
import com.example.circle_platform_api.service.CircleService;
import com.example.circle_platform_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CircleApplicationService {

    private final CircleService circleService;
    private final UserService userService;
    private final CircleCreateApplicationService circleCreateApplicationService;
    private final CircleConvert circleConvert;

    @Transactional(rollbackFor = Exception.class)
    public void createApplication(CircleCreateApplicationDTO dto) {
        // 创建社团申请
        CircleCreateApplication circleCreateApplication = circleConvert.createToEntity(dto);
        circleCreateApplicationService.save(circleCreateApplication);

    }

    @Transactional(rollbackFor = Exception.class)
    public void updateDraft(Long applicationId, CircleCreateApplicationUpdateDTO dto) {
        CircleCreateApplication circleCreateApplication = circleCreateApplicationService.getById(applicationId);

        if (circleCreateApplication == null) {
            throw new BusinessException(ResultCode.APPLICATION_NOT_FOUND);
        }

        if (circleCreateApplication.getStatus() != ApplicationStatus.DRAFT) {
            throw new BusinessException(ResultCode.APPLICATION_CAN_NOT_UPDATE);
        }

        CircleCreateApplication updateApplication = getCircleCreateApplication(dto, circleCreateApplication);
        circleCreateApplicationService.updateById(updateApplication);
    }

    @NonNull
    private CircleCreateApplication getCircleCreateApplication(CircleCreateApplicationUpdateDTO dto, CircleCreateApplication circleCreateApplication) {
        CircleCreateApplication updateApplication = new CircleCreateApplication();
        updateApplication.setId(circleCreateApplication.getId());
        if (!dto.getName().isBlank()) {
            updateApplication.setName(dto.getName());
        }
        if (dto.getCategoryId() != null) {
            updateApplication.setCategoryId(dto.getCategoryId());
        }
        if (!dto.getDescription().isBlank()) {
            updateApplication.setDescription(dto.getDescription());
        }
        if (!dto.getApplyReason().isBlank()) {
            updateApplication.setApplyReason(dto.getApplyReason());
        }
        return updateApplication;
    }

    @Transactional(readOnly = true)
    public PageResult<CircleCreateApplicationVo> getApplications(CircleCreateApplicationPageDTO dto) {

        long pageNum = Math.max(1, dto.getPageNum());
        Page<CircleCreateApplication> page = Page.of(pageNum, dto.getPageSize());

        User user = userService.getById(SecurityUtils.getCurrentUserId());

        circleCreateApplicationService.lambdaQuery()
                .eq(CircleCreateApplication::getUserId, SecurityUtils.getCurrentUser())
                .eq(CircleCreateApplication::getStatus, ApplicationStatus.fromCode(dto.getStatus()))
                .orderByDesc(CircleCreateApplication::getCreatedAt)
                .page(page);

        String applyUserName = !user.getNickname().isBlank() ? user.getNickname() : user.getUsername();

        List<CircleCreateApplicationVo> vos = page.getRecords().stream()
                .map(c -> new CircleCreateApplicationVo(
                        c.getId(),
                        user.getId(),
                        applyUserName,
                        c.getCategoryId(),
                        c.getName(),
                        c.getStatus(),
                        c.getCreatedAt()
                ))
                .toList();
        return PageResult.of(page.getTotal(), page.getCurrent(), page.getSize(), page.getPages(), vos);
    }

    @Transactional(rollbackFor = Exception.class)
    public void sendCircleCreateApplication(Long applicationId) {

        long pendingCount = circleCreateApplicationService.lambdaQuery()
                .eq(CircleCreateApplication::getUserId, SecurityUtils.getCurrentUserId())
                .eq(CircleCreateApplication::getStatus, ApplicationStatus.PENDING)
                .count();

        if (pendingCount > 0) {
            throw new BusinessException(ResultCode.PENDING_APPLICATION_EXISTS.getCode(), "您有正在审核中的建社申请，请勿重复提交");
        }

        CircleCreateApplication circleCreateApplication = circleCreateApplicationService.getById(applicationId);

        if (circleCreateApplication == null) {
            throw new BusinessException(ResultCode.APPLICATION_NOT_FOUND);
        }

        if (circleCreateApplication.getStatus() != ApplicationStatus.DRAFT) {
            throw new BusinessException(ResultCode.APPLICATION_APPLIED.getCode(), "草稿已经被处理，不能重复申请");
        }

        CircleCreateApplication updateApplication = new CircleCreateApplication();
        updateApplication.setId(circleCreateApplication.getId());
        updateApplication.setStatus(ApplicationStatus.PENDING);
        circleCreateApplicationService.updateById(updateApplication);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeCircleCreateApplication(Long applicationId) {
        CircleCreateApplication circleCreateApplication = circleCreateApplicationService.getById(applicationId);

        if (circleCreateApplication == null) {
            throw new BusinessException(ResultCode.APPLICATION_NOT_FOUND);
        }

        SecurityUser user = SecurityUtils.getCurrentUser();

        if (!Objects.equals(circleCreateApplication.getUserId(), user.getUserId())) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "无权删除此请求");
        }

        if (circleCreateApplication.getStatus() == ApplicationStatus.PENDING) {
            throw new BusinessException(ResultCode.APPLICATION_CAN_NOT_REMOVE);
        }

        circleCreateApplicationService.removeById(applicationId);
    }

    @Transactional(readOnly = true)
    public PageResult<CircleVo> getCircleList(CircleQueryDTO dto) {

        long pageNum = Math.max(1, dto.getPageNum());
        // 构建分页
        Page<Circle> page = Page.of(pageNum, dto.getPageSize());

        circleService.lambdaQuery()
                .eq(Circle::getStatus, CircleStatus.NORMAL)
                .eq(dto.getCategoryId() != null, Circle::getCategoryId, dto.getCategoryId())
                .like(StringUtils.hasText(dto.getKeywords()), Circle::getName, dto.getKeywords())
                .page(page);

        if (page.getRecords().isEmpty()) {
            return PageResult.empty(pageNum, dto.getPageSize());
        }

        // 组装vo
        List<CircleVo> vos = page.getRecords().stream()
                .map(circle -> new CircleVo(
                        circle.getId(),
                        circle.getName(),
                        circle.getCategoryId(),
                        circle.getMemberCount())).toList();

        return PageResult.of(page.getTotal(), page.getCurrent(), page.getSize(), page.getPages(), vos);
    }

    @Transactional(readOnly = true)
    public CircleDetailVo getCircleDetails(Long circleId) {

        Circle circle = circleService.getById(circleId);

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
                circle.getCategoryId(),
                circle.getOwnerId(),
                ownerName,
                circle.getMemberCount()
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void dissolveCircle(Long circleId) {

        SecurityUser user = SecurityUtils.getCurrentUser();

        Circle circle = circleService.getById(circleId);

        if (circle == null) {
            throw new BusinessException(ResultCode.CIRCLE_NOT_FOUND);
        }

        if (circle.getStatus() == CircleStatus.DISSOLVED) {
            throw new BusinessException(ResultCode.CIRCLE_DISSOLVED.getCode(), "社团已解散，无法进行操作");
        }

        if (!Objects.equals(circle.getOwnerId(), user.getUserId()) && !user.getRole().equals(UserRole.USER.name())) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "您没有权限进行此操作");
        }

        circleService.lambdaUpdate()
                .eq(Circle::getId, circle.getId())
                .set(Circle::getStatus, CircleStatus.DISSOLVED)
                .update();
    }

    public void updateCircle(Long circleId, CircleUpdateDTO dto) {

        Circle circle = circleService.getById(circleId);

        if (circle == null) {
            throw new BusinessException(ResultCode.CIRCLE_NOT_FOUND);
        }

        if (circle.getStatus() == CircleStatus.DISSOLVED) {
            throw new BusinessException(ResultCode.CIRCLE_DISSOLVED.getCode(), "社团已解散，无法进行操作");
        }

        boolean success = circleService.lambdaUpdate()
                .eq(Circle::getId, circleId)
                .set(StringUtils.hasText(dto.getName()), Circle::getName, dto.getName())
                .set(StringUtils.hasText(dto.getDescription()), Circle::getDescription, dto.getDescription())
                .set(Objects.nonNull(dto.getCategoryId()), Circle::getCategoryId, dto.getCategoryId())
                .update();

        if (!success) {
            throw new BusinessException(ResultCode.CIRCLE_UPDATE_FAIL);
        }

    }

}
