package com.example.circle_platform_api.controller;

import com.example.circle_platform_api.application.CircleApplicationService;
import com.example.circle_platform_api.common.response.ApiResponse;
import com.example.circle_platform_api.common.response.PageResult;
import com.example.circle_platform_api.model.dto.*;
import com.example.circle_platform_api.model.vo.CircleCreateApplicationVo;
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

    @PostMapping("/application")
    public ApiResponse<Void> createApplication(@RequestBody @NotNull @Valid CircleCreateApplicationDTO dto) {
        circleApplicationService.createApplication(dto);
        return ApiResponse.success();
    }

    @PutMapping("/application/{id}/update")
    public ApiResponse<Void> updateDraftApplication(@PathVariable Long id, @RequestBody @NotNull @Valid CircleCreateApplicationUpdateDTO dto) {
        circleApplicationService.updateDraft(id, dto);
        return ApiResponse.success();
    }

    @PostMapping("/application/{id}/send")
    public ApiResponse<Void> sendApplication(@PathVariable Long id) {
        circleApplicationService.sendCircleCreateApplication(id);
        return ApiResponse.success();
    }

    @DeleteMapping("/application/{id}")
    public ApiResponse<Void> removeApplication(@PathVariable Long id){
        circleApplicationService.removeCircleCreateApplication(id);
        return ApiResponse.success();
    }

    @GetMapping("/application/list")
    public ApiResponse<PageResult<CircleCreateApplicationVo>> getApplications(CircleCreateApplicationQueryDTO dto) {
        return ApiResponse.success(circleApplicationService.getApplications(dto));
    }

    @GetMapping("/list")
    public ApiResponse<PageResult<CircleVo>> getCirclePages(@NotNull @Valid CircleQueryDTO dto) {
        return ApiResponse.success(circleApplicationService.getCircleList(dto));
    }

    @GetMapping("/{circleId}")
    public ApiResponse<CircleDetailVo> getCircleDetails(@PathVariable Long circleId) {
        return ApiResponse.success(circleApplicationService.getCircleDetails(circleId));
    }

    @PutMapping("/{circleId}/dissolve")
    public ApiResponse<Void> dissolveCircle(@PathVariable Long circleId) {
        circleApplicationService.dissolveCircle(circleId);
        return ApiResponse.success();
    }

    @PutMapping("/{circleId}")
    public ApiResponse<Void> updateCircle(@PathVariable("circleId") Long id, @RequestBody @NotNull @Valid CircleUpdateDTO dto) {
        circleApplicationService.updateCircle(id, dto);
        return ApiResponse.success();
    }

}
