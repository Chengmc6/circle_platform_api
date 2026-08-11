package com.example.circle_platform_api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.circle_platform_api.enums.ApplicationStatus;
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
@TableName("circle_join_application")
@Schema(name = "CircleJoinApplication", description = "")
public class CircleJoinApplication implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 申请ID
     */
    @Schema(description = "申请ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 社团ID
     */
    @TableField("circle_id")
    @Schema(description = "社团ID")
    private Long circleId;

    /**
     * 申请人ID
     */
    @TableField("user_id")
    @Schema(description = "申请人ID")
    private Long userId;

    /**
     * 申请理由
     */
    @TableField("apply_reason")
    @Schema(description = "申请理由")
    private String applyReason;

    /**
     * 0待审核 1已通过 2已拒绝
     */
    @TableField("status")
    @Schema(description = "0待审核 1已通过 2已拒绝")
    private ApplicationStatus status;

    /**
     * 审核人ID（社团无管理员时由系统管理员审核）
     */
    @TableField("reviewer_id")
    @Schema(description = "审核人ID（社团无管理员时由系统管理员审核）")
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
