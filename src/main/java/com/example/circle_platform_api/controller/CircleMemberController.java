package com.example.circle_platform_api.controller;

import com.example.circle_platform_api.application.CircleMemberApplicationService;
import com.example.circle_platform_api.common.response.ApiResponse;
import com.example.circle_platform_api.common.response.PageResult;
import com.example.circle_platform_api.model.dto.CircleJoinApplicationDTO;
import com.example.circle_platform_api.model.dto.CircleJoinApplicationQueryDTO;
import com.example.circle_platform_api.model.dto.CircleJoinApplicationReviewDTO;
import com.example.circle_platform_api.model.vo.CircleJoinApplicationVo;
import com.example.circle_platform_api.model.vo.CircleMemberVo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
public class CircleMemberController {

    private final CircleMemberApplicationService memberApplicationService;

    @PostMapping("/{circleId}/join/create")
    public ApiResponse<Void> createJoinApplication(@PathVariable Long circleId, @RequestBody @NotNull @Valid CircleJoinApplicationDTO dto) {
        memberApplicationService.createJoinApplication(circleId, dto);
        return ApiResponse.success();
    }

    @PostMapping("/{circleId}/join/approve/{id}")
    public ApiResponse<Void> approve(@PathVariable Long circleId, @PathVariable Long id, @RequestBody @NotNull @Valid CircleJoinApplicationReviewDTO dto) {
        memberApplicationService.approve(circleId, id, dto);
        return ApiResponse.success();
    }

    @PostMapping("/{circleId}/join/reject/{id}")
    public ApiResponse<Void> reject(@PathVariable Long circleId, @PathVariable Long id, @RequestBody @NotNull @Valid CircleJoinApplicationReviewDTO dto) {
        memberApplicationService.reject(circleId, id, dto);
        return ApiResponse.success();
    }

    @PostMapping("/{circleId}")
    public ApiResponse<Void> leave(@PathVariable Long circleId) {
        memberApplicationService.leave(circleId);
        return ApiResponse.success();
    }

    @GetMapping("/{circleId}/join")
    public ApiResponse<PageResult<CircleJoinApplicationVo>> getApplications(@PathVariable Long circleId, @NotNull @Valid CircleJoinApplicationQueryDTO dto) {
        return ApiResponse.success(memberApplicationService.getApplications(circleId, dto));
    }

    @GetMapping("/{circleId}/members")
    public ApiResponse<List<CircleMemberVo>> getMembers(@PathVariable Long circleId) {
        return ApiResponse.success(memberApplicationService.getMembers(circleId));
    }

    @DeleteMapping("/{circleId}/members/{memberId}")
    public ApiResponse<Void> removeMember(@PathVariable Long circleId, @PathVariable Long memberId) {
        memberApplicationService.removeCircleMember(circleId, memberId);
        return ApiResponse.success();
    }

}
