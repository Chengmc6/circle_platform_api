package com.example.circle_platform_api.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.circle_platform_api.common.exception.BusinessException;
import com.example.circle_platform_api.common.response.PageResult;
import com.example.circle_platform_api.common.response.ResultCode;
import com.example.circle_platform_api.entity.Circle;
import com.example.circle_platform_api.entity.CircleJoinApplication;
import com.example.circle_platform_api.entity.CircleMember;
import com.example.circle_platform_api.entity.User;
import com.example.circle_platform_api.enums.ApplicationStatus;
import com.example.circle_platform_api.enums.CircleMemberRole;
import com.example.circle_platform_api.enums.CircleStatus;
import com.example.circle_platform_api.enums.UserStatus;
import com.example.circle_platform_api.model.dto.CircleJoinApplicationDTO;
import com.example.circle_platform_api.model.dto.CircleJoinApplicationQueryDTO;
import com.example.circle_platform_api.model.dto.CircleJoinApplicationReviewDTO;
import com.example.circle_platform_api.model.vo.CircleJoinApplicationVo;
import com.example.circle_platform_api.model.vo.CircleMemberVo;
import com.example.circle_platform_api.security.utils.SecurityUtils;
import com.example.circle_platform_api.service.CircleJoinApplicationService;
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
public class CircleMemberApplicationService {

    private final CircleService circleService;
    private final CircleMemberService memberService;
    private final UserService userService;
    private final CircleJoinApplicationService joinApplicationService;

    @Transactional(rollbackFor = Exception.class)
    public void createJoinApplication(Long circleId, CircleJoinApplicationDTO dto) {

        Circle circle = circleService.getById(circleId);

        if (circle == null) {
            throw new BusinessException(ResultCode.CIRCLE_NOT_FOUND);
        }

        if (circle.getStatus() != CircleStatus.NORMAL) {
            throw new BusinessException(ResultCode.CIRCLE_IS_NOT_NORMAL);
        }

        User user = userService.getById(dto.getUserId());

        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if (user.getStatus() == UserStatus.GRADUATED) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        boolean alreadyMember = memberService.lambdaQuery()
                .eq(CircleMember::getCircleId, circle.getId())
                .eq(CircleMember::getUserId, user.getId())
                .isNull(CircleMember::getDeleteAt)   // 如果用了逻辑删除
                .exists();

        if (alreadyMember) {
            throw new BusinessException(ResultCode.ALREADY_MEMBER);
        }

        CircleJoinApplication joinApplication = new CircleJoinApplication();
        joinApplication.setCircleId(circleId);
        joinApplication.setUserId(dto.getUserId());
        if (dto.getApplyReason() != null && !dto.getApplyReason().isBlank()) {
            joinApplication.setApplyReason(dto.getApplyReason());
        }

        joinApplicationService.save(joinApplication);
    }

    @Transactional(rollbackFor = Exception.class)
    public void approve(Long circleId, Long applicationId, CircleJoinApplicationReviewDTO dto) {

        CircleJoinApplication joinApplication = joinApplicationService.getById(applicationId);

        if (joinApplication == null) {
            throw new BusinessException(ResultCode.APPLICATION_NOT_FOUND);
        }

        if (joinApplication.getStatus() != ApplicationStatus.PENDING) {
            throw new BusinessException(ResultCode.APPLICATION_PROCESSED);
        }

        if (!Objects.equals(joinApplication.getCircleId(), circleId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        User applicant = userService.getById(joinApplication.getUserId());
        if (applicant == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if (applicant.getStatus() != UserStatus.ENROLLED) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        Circle circle = circleService.getById(circleId);

        if (circle == null) {
            throw new BusinessException(ResultCode.CIRCLE_NOT_FOUND);
        }

        if (circle.getStatus() != CircleStatus.NORMAL) {
            throw new BusinessException(ResultCode.CIRCLE_IS_NOT_NORMAL);
        }

        boolean alreadyMember = memberService.lambdaQuery()
                .eq(CircleMember::getCircleId, circle.getId())
                .eq(CircleMember::getUserId, applicant.getId())
                .isNull(CircleMember::getDeleteAt)   // 如果用了逻辑删除
                .exists();

        if (alreadyMember) {
            throw new BusinessException(ResultCode.ALREADY_MEMBER);
        }

        CircleMember member = memberService.lambdaQuery()
                .eq(CircleMember::getCircleId, circle.getId())
                .eq(CircleMember::getUserId, SecurityUtils.getCurrentUserId())
                .ne(CircleMember::getRole, CircleMemberRole.MEMBER)
                .one();

        if (member == null) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        CircleMember app = new CircleMember();
        app.setUserId(applicant.getId());
        app.setCircleId(circleId);
        memberService.save(app);

        circleService.lambdaUpdate()
                .eq(Circle::getId, circleId)
                .setSql("member_count=member_count+1")
                .update();

        joinApplicationService.lambdaUpdate()
                .eq(CircleJoinApplication::getId, applicationId)
                .set(CircleJoinApplication::getStatus, ApplicationStatus.APPROVED)
                .set(CircleJoinApplication::getReviewerId, dto.getReviewId())
                .set(StringUtils.hasText(dto.getReviewMark()), CircleJoinApplication::getReviewRemark, dto.getReviewMark())
                .set(CircleJoinApplication::getReviewedAt, LocalDateTime.now())
                .update();

    }

    @Transactional(rollbackFor = Exception.class)
    public void reject(Long circleId, Long applicationId, CircleJoinApplicationReviewDTO dto) {
        CircleJoinApplication joinApplication = joinApplicationService.getById(applicationId);

        if (joinApplication == null) {
            throw new BusinessException(ResultCode.APPLICATION_NOT_FOUND);
        }

        if (joinApplication.getStatus() != ApplicationStatus.PENDING) {
            throw new BusinessException(ResultCode.APPLICATION_PROCESSED);
        }

        if (!Objects.equals(joinApplication.getCircleId(), circleId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        User applicant = userService.getById(joinApplication.getUserId());
        if (applicant == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        if (applicant.getStatus() != UserStatus.ENROLLED) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        Circle circle = circleService.getById(circleId);

        if (circle == null) {
            throw new BusinessException(ResultCode.CIRCLE_NOT_FOUND);
        }

        if (circle.getStatus() != CircleStatus.NORMAL) {
            throw new BusinessException(ResultCode.CIRCLE_IS_NOT_NORMAL);
        }

        CircleMember member = memberService.lambdaQuery()
                .eq(CircleMember::getCircleId, circleId)
                .eq(CircleMember::getUserId, SecurityUtils.getCurrentUserId())
                .ne(CircleMember::getRole, CircleMemberRole.MEMBER)
                .one();

        if (member == null) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        joinApplicationService.lambdaUpdate()
                .eq(CircleJoinApplication::getId, applicationId)
                .set(CircleJoinApplication::getStatus, ApplicationStatus.REJECTED)
                .set(CircleJoinApplication::getReviewerId, dto.getReviewId())
                .set(StringUtils.hasText(dto.getReviewMark()), CircleJoinApplication::getReviewRemark, dto.getReviewMark())
                .set(CircleJoinApplication::getReviewedAt, LocalDateTime.now())
                .update();
    }

    @Transactional(rollbackFor = Exception.class)
    public void leave(Long circleId) {

        Circle circle = circleService.getById(circleId);

        if (circle == null) {
            throw new BusinessException(ResultCode.CIRCLE_NOT_FOUND);
        }

        if (circle.getStatus() == CircleStatus.DISSOLVED) {
            throw new BusinessException(ResultCode.CIRCLE_DISSOLVED);
        }

        CircleMember member = memberService.lambdaQuery()
                .eq(CircleMember::getCircleId, circleId)
                .eq(CircleMember::getUserId, SecurityUtils.getCurrentUserId())
                .one();

        if (member == null) {
            throw new BusinessException(ResultCode.IS_NOT_CIRCLE_MEMBER);
        }

        if (member.getRole() == CircleMemberRole.PRESIDENT) {
            throw new BusinessException(ResultCode.PRESIDENT_CAN_NOT_LEAVE);
        }

        memberService.removeById(member.getId());

        circleService.lambdaUpdate()
                .eq(Circle::getId, circleId)
                .setSql("member_count=member_count-1")
                .update();
    }

    public PageResult<CircleJoinApplicationVo> getApplications(Long circleId, CircleJoinApplicationQueryDTO dto) {

        Circle circle = circleService.getById(circleId);

        if (circle == null) {
            throw new BusinessException(ResultCode.CIRCLE_NOT_FOUND);
        }

        if (circle.getStatus() == CircleStatus.DISSOLVED) {
            throw new BusinessException(ResultCode.CIRCLE_DISSOLVED);
        }

        Page<CircleJoinApplication> page = Page.of(dto.getPageNum(), dto.getPageSize());

        joinApplicationService.lambdaQuery()
                .eq(CircleJoinApplication::getCircleId, circleId)
                .eq(CircleJoinApplication::getStatus, ApplicationStatus.fromCode(dto.getStatus()))
                .page(page);

        if (page.getRecords().isEmpty()) {
            return PageResult.empty(dto.getPageNum(), dto.getPageSize());
        }

        Set<Long> userIds = page.getRecords().stream()
                .map(CircleJoinApplication::getUserId)
                .collect(Collectors.toSet());

        Set<Long> reviewerIds = page.getRecords().stream()
                .map(CircleJoinApplication::getReviewerId)
                .collect(Collectors.toSet());

        List<User> users = userService.listByIds(userIds);
        List<User> reviewers = userService.listByIds(reviewerIds);

        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, u -> u));
        Map<Long, User> reviewerMap = reviewers.stream().collect(Collectors.toMap(User::getId, u -> u));

        List<CircleJoinApplicationVo> vos = page.getRecords().stream()
                .map(j -> {
                    User user = userMap.get(j.getUserId());
                    User reviewer = reviewerMap.get(j.getReviewerId());
                    return new CircleJoinApplicationVo(
                            j.getId(),
                            circleId,
                            j.getUserId(),
                            StringUtils.hasText(user.getNickname()) ? user.getNickname() : user.getUsername(),
                            j.getApplyReason(),
                            j.getStatus(),
                            j.getCreatedAt(),
                            j.getReviewerId(),
                            StringUtils.hasText(reviewer.getNickname()) ? reviewer.getNickname() : reviewer.getUsername(),
                            j.getReviewRemark(),
                            j.getReviewedAt()
                    );
                })
                .toList();


        return PageResult.of(page.getTotal(), page.getCurrent(), page.getSize(), page.getPages(), vos);
    }

    public List<CircleMemberVo> getMembers(Long circleId) {

        Circle circle = circleService.getById(circleId);

        if (circle == null) {
            throw new BusinessException(ResultCode.CIRCLE_NOT_FOUND);
        }

        if (circle.getStatus() == CircleStatus.DISSOLVED) {
            throw new BusinessException(ResultCode.CIRCLE_DISSOLVED);
        }

        List<CircleMember> members = memberService.lambdaQuery()
                .eq(CircleMember::getCircleId, circleId)
                .list();

        Set<Long> userIds = members.stream()
                .filter(Objects::nonNull)
                .map(CircleMember::getUserId)
                .collect(Collectors.toSet());

        List<User> users = userService.listByIds(userIds);

        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        return members.stream()
                .map(m -> {
                    User user = userMap.get(m.getUserId());
                    return new CircleMemberVo(
                            m.getId(),
                            m.getCircleId(),
                            m.getUserId(),
                            user.getUsername(),
                            m.getRole()
                    );
                })
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeCircleMember(Long circleId, Long memberId) {

        Circle circle = circleService.getById(circleId);

        if (circle == null) {
            throw new BusinessException(ResultCode.CIRCLE_NOT_FOUND);
        }

        if (circle.getStatus() == CircleStatus.DISSOLVED) {
            throw new BusinessException(ResultCode.CIRCLE_DISSOLVED);
        }

        CircleMember current = memberService.lambdaQuery()
                .eq(CircleMember::getCircleId, circleId)
                .eq(CircleMember::getUserId, SecurityUtils.getCurrentUserId())
                .one();

        if (current == null || current.getRole() == CircleMemberRole.MEMBER) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        CircleMember member = memberService.getById(memberId);

        if (member == null) {
            throw new BusinessException(ResultCode.MEMBER_NOT_FOUND);
        }

        if (Objects.equals(member.getUserId(), SecurityUtils.getCurrentUserId())) {
            throw new BusinessException(ResultCode.CAN_NOT_REMOVE_YOURSELF);
        }

        if (member.getRole() != CircleMemberRole.MEMBER) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        memberService.removeById(memberId);

    }

}
