package com.example.circle_platform_api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author GAOMING
 * @since 2026-08-09
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("facility_reservation")
@Schema(name = "FacilityReservation", description = "")
public class FacilityReservation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 预约ID
     */
    @Schema(description = "预约ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设施ID
     */
    @TableField("facility_id")
    @Schema(description = "设施ID")
    private Long facilityId;

    /**
     * 关联活动ID（一个活动可预约多个设施）
     */
    @TableField("event_id")
    @Schema(description = "关联活动ID（一个活动可预约多个设施）")
    private Long eventId;

    /**
     * 预约社团ID
     */
    @TableField("circle_id")
    @Schema(description = "预约社团ID")
    private Long circleId;

    /**
     * 预约人ID
     */
    @TableField("user_id")
    @Schema(description = "预约人ID")
    private Long userId;

    /**
     * 预约开始时间
     */
    @TableField("start_time")
    @Schema(description = "预约开始时间")
    private LocalDateTime startTime;

    /**
     * 预约结束时间
     */
    @TableField("end_time")
    @Schema(description = "预约结束时间")
    private LocalDateTime endTime;

    /**
     * 0待审核 1已通过 2已拒绝
     */
    @TableField("status")
    @Schema(description = "0待审核 1已通过 2已拒绝")
    private Integer status;

    /**
     * 审核人ID
     */
    @TableField("reviewer_id")
    @Schema(description = "审核人ID")
    private Long reviewerId;

    /**
     * 审核备注
     */
    @TableField("review_remark")
    @Schema(description = "审核备注")
    private String reviewRemark;

    /**
     * 审核时间
     */
    @TableField("reviewed_at")
    @Schema(description = "审核时间")
    private LocalDateTime reviewedAt;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(value = "created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 删除时间（NULL：未删除；非NULL：已逻辑删除）
     */
    @JsonIgnore
    @TableLogic
    @TableField("delete_at")
    @Schema(description = "删除时间（NULL：未删除；非NULL：已逻辑删除）")
    private LocalDateTime deleteAt;
}
