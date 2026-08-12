package com.example.circle_platform_api.application;

import com.example.circle_platform_api.entity.CircleCreateApplication;
import com.example.circle_platform_api.mapstruct.CircleConvert;
import com.example.circle_platform_api.model.dto.CircleCreateApplicationDTO;
import com.example.circle_platform_api.service.CircleCreateApplicationService;
import com.example.circle_platform_api.service.CircleService;
import com.example.circle_platform_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CircleApplicationService {

    private final CircleService circleService;
    private final UserService userService;
    private final CircleCreateApplicationService circleCreateApplicationService;
    private final CircleConvert circleConvert;

    @Transactional(rollbackFor = Exception.class)
    public void createApplication(CircleCreateApplicationDTO dto){

        CircleCreateApplication circleCreateApplication=circleConvert.createToEntity(dto);

        circleCreateApplicationService.save(circleCreateApplication);

    }

}
