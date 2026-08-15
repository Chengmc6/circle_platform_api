package com.example.circle_platform_api.application;

import com.example.circle_platform_api.service.CircleJoinApplicationService;
import com.example.circle_platform_api.service.CircleMemberService;
import com.example.circle_platform_api.service.CircleService;
import com.example.circle_platform_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CircleMemberApplicationService {

    private final CircleService circleService;
    private final CircleMemberService memberService;
    private final UserService userService;
    private final CircleJoinApplicationService joinApplicationService;



}
