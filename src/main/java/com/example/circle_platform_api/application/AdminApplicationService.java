package com.example.circle_platform_api.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.circle_platform_api.common.exception.BusinessException;
import com.example.circle_platform_api.common.response.PageResult;
import com.example.circle_platform_api.common.response.ResultCode;
import com.example.circle_platform_api.entity.Circle;
import com.example.circle_platform_api.entity.CircleCreateApplication;
import com.example.circle_platform_api.entity.CircleMember;
import com.example.circle_platform_api.entity.User;
import com.example.circle_platform_api.enums.ApplicationStatus;
import com.example.circle_platform_api.enums.CircleMemberRole;
import com.example.circle_platform_api.enums.UserStatus;
import com.example.circle_platform_api.model.dto.CircleCreateApplicationQueryDTO;
import com.example.circle_platform_api.model.vo.CircleCreateApplicationVo;
import com.example.circle_platform_api.service.CircleCreateApplicationService;
import com.example.circle_platform_api.service.CircleMemberService;
import com.example.circle_platform_api.service.CircleService;
import com.example.circle_platform_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminApplicationService {

    private final UserService userService;
    private final CircleCreateApplicationService circleCreateApplicationService;
    private final CircleService circleService;
    private final CircleMemberService memberService;

    @Transactional(rollbackFor = Exception.class)
    public void circleCreateApplicationApprove(Long applicationId, Long adminId, String remark) {

        CircleCreateApplication application = circleCreateApplicationService.getById(applicationId);

        if (application == null) {
            throw new BusinessException(ResultCode.APPLICATION_NOT_FOUND);
        }

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new BusinessException(ResultCode.APPLICATION_PROCESSED);
        }

        User applicant = userService.getById(application.getUserId());
        if (applicant == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if (applicant.getStatus() != UserStatus.ENROLLED) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        // 更新申请表
        CircleCreateApplication createApplication = new CircleCreateApplication();
        createApplication.setId(application.getId());
        if (StringUtils.hasText(remark)) {
            createApplication.setReviewRemark(remark);
        }
        createApplication.setReviewerId(adminId);
        createApplication.setReviewedAt(LocalDateTime.now());
        createApplication.setStatus(ApplicationStatus.APPROVED);

        circleCreateApplicationService.updateById(createApplication);

        // 创建社团
        Circle circle = new Circle();
        circle.setName(application.getName());
        circle.setDescription(application.getDescription());
        circle.setCategoryId(application.getCategoryId());
        circle.setOwnerId(application.getUserId());
        circle.setMemberCount(1);
        circleService.save(circle);

        // 添加社团成员(社长)
        CircleMember member = new CircleMember();
        member.setCircleId(circle.getId());
        member.setRole(CircleMemberRole.PRESIDENT);
        member.setJoinedAt(LocalDateTime.now());
        member.setUserId(application.getUserId());
        memberService.save(member);

    }

    @Transactional(rollbackFor = Exception.class)
    public void circleCreateApplicationRejected(Long applicationId, Long adminId, String remark) {
        CircleCreateApplication application = circleCreateApplicationService.getById(applicationId);

        if (application == null) {
            throw new BusinessException(ResultCode.APPLICATION_NOT_FOUND);
        }

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new BusinessException(ResultCode.APPLICATION_PROCESSED);
        }

        // 更新申请表
        CircleCreateApplication createApplication = new CircleCreateApplication();
        createApplication.setId(application.getId());
        if (StringUtils.hasText(remark)) {
            createApplication.setReviewRemark(remark);
        }
        createApplication.setReviewerId(adminId);
        createApplication.setReviewedAt(LocalDateTime.now());
        createApplication.setStatus(ApplicationStatus.REJECTED);

        circleCreateApplicationService.updateById(createApplication);
    }

    public PageResult<CircleCreateApplicationVo> getApplications(CircleCreateApplicationQueryDTO dto) {

        long pageNum = Math.max(1, dto.getPageNum());
        Page<CircleCreateApplication> page = Page.of(pageNum, dto.getPageSize());

        circleCreateApplicationService.lambdaQuery()
                .eq(CircleCreateApplication::getStatus, ApplicationStatus.fromCode(dto.getStatus()))
                .orderByDesc(CircleCreateApplication::getCreatedAt)
                .page(page);

        if (page.getRecords().isEmpty()) {
            return PageResult.empty(pageNum, dto.getPageSize());
        }

        Set<Long> userIds = page.getRecords().stream()
                .map(CircleCreateApplication::getUserId)
                .collect(Collectors.toSet());

        List<User> users = userService.listByIds(userIds);

        Map<Long, User> userMap = users.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(User::getId, u -> u));

        List<CircleCreateApplicationVo> vos = page.getRecords().stream()
                .map(c -> {
                    User user = userMap.get(c.getUserId());
                    String applyUserName = "已注销用户";
                    Long userId = null;
                    if (user != null) {
                        applyUserName = Objects.nonNull(user.getNickname()) ? user.getNickname() : user.getUsername();
                        userId = user.getId();
                    }
                    return new CircleCreateApplicationVo(
                            c.getId(),
                            userId,
                            applyUserName,
                            c.getCategoryId(),
                            c.getName(),
                            c.getStatus(),
                            c.getCreatedAt()
                    );
                })
                .toList();


        return PageResult.of(page.getTotal(), page.getCurrent(), page.getSize(), page.getPages(), vos);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeCircleCreateApplication(Long applicationId) {
        CircleCreateApplication circleCreateApplication = circleCreateApplicationService.getById(applicationId);

        if (circleCreateApplication == null) {
            throw new BusinessException(ResultCode.APPLICATION_NOT_FOUND);
        }

        circleCreateApplicationService.removeById(applicationId);

    }

}
