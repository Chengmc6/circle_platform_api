package com.example.circle_platform_api.application;

import com.example.circle_platform_api.common.exception.BusinessException;
import com.example.circle_platform_api.common.response.ResultCode;
import com.example.circle_platform_api.entity.Circle;
import com.example.circle_platform_api.entity.CircleCreateApplication;
import com.example.circle_platform_api.entity.CircleMember;
import com.example.circle_platform_api.enums.ApplicationStatus;
import com.example.circle_platform_api.enums.CircleMemberRole;
import com.example.circle_platform_api.service.CircleCreateApplicationService;
import com.example.circle_platform_api.service.CircleMemberService;
import com.example.circle_platform_api.service.CircleService;
import com.example.circle_platform_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminApplicationService {

    private final UserService userService;
    private final CircleCreateApplicationService circleCreateApplicationService;
    private final CircleService circleService;
    private final CircleMemberService memberService;

    @Transactional(rollbackFor = Exception.class)
    public void circleCreateApplicationApprove(Long applicationId,Long adminId,String remark){

        CircleCreateApplication application=circleCreateApplicationService.getById(applicationId);

        if(application==null){
            throw new BusinessException(ResultCode.APPLICATION_NOT_FOUND);
        }

        if (application.getStatus()!= ApplicationStatus.PENDING){
            throw new BusinessException(ResultCode.APPLICATION_PROCESSED);
        }

        // 更新申请表
        if(StringUtils.hasText(remark)){
            application.setReviewRemark(remark);
        }
        application.setReviewerId(adminId);
        application.setReviewedAt(LocalDateTime.now());
        application.setStatus(ApplicationStatus.APPROVED);

        circleCreateApplicationService.updateById(application);

        // 创建社团
        Circle circle=new Circle();
        circle.setName(application.getName());
        circle.setDescription(application.getDescription());
        circle.setCategoryId(application.getCategoryId());
        circle.setOwnerId(application.getUserId());
        circle.setMemberCount(1);
        circleService.save(circle);

        // 添加社团成员(社长)
        CircleMember member=new CircleMember();
        member.setCircleId(circle.getId());
        member.setRole(CircleMemberRole.PRESIDENT);
        member.setJoinedAt(LocalDateTime.now());
        member.setUserId(application.getUserId());
        memberService.save(member);

    }

}
