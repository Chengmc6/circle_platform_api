package com.example.circle_platform_api.controller;

import com.example.circle_platform_api.application.CircleApplicationService;
import com.example.circle_platform_api.common.response.ApiResponse;
import com.example.circle_platform_api.common.response.PageResult;
import com.example.circle_platform_api.model.dto.CircleCreateApplicationDTO;
import com.example.circle_platform_api.model.dto.CircleQueryDTO;
import com.example.circle_platform_api.model.dto.CircleUpdateDTO;
import com.example.circle_platform_api.model.vo.CircleDetailVo;
import com.example.circle_platform_api.model.vo.CircleVo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author GAOMING
 * @since 2026-08-09
 */
@Validated
@RestController
@RequestMapping("/circle")
@RequiredArgsConstructor
public class CircleController {

    private final CircleApplicationService circleApplicationService;

    @PostMapping("/create")
    public ApiResponse<Void> createApplication(@RequestBody @NotNull @Valid CircleCreateApplicationDTO dto) {
        circleApplicationService.createApplication(dto);
        return ApiResponse.success();
    }

    @GetMapping("/list")
    public ApiResponse<PageResult<CircleVo>> getCirclePages(@NotNull @Valid CircleQueryDTO dto) {
        return ApiResponse.success(circleApplicationService.getCircleList(dto));
    }

    @GetMapping("/{id}")
    public ApiResponse<CircleDetailVo> getCircleDetails(@PathVariable Long id) {
        return ApiResponse.success(circleApplicationService.getCircleDetails(id));
    }

    @PutMapping("/{id}/dissolve")
    public ApiResponse<Void> dissolveCircle(@PathVariable Long id) {
        circleApplicationService.dissolveCircle(id);
        return ApiResponse.success();
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> updateCircle(@PathVariable Long id, @RequestBody @NotNull @Valid CircleUpdateDTO dto) {
        circleApplicationService.updateCircle(id, dto);
        return ApiResponse.success();
    }

}
