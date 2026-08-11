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
@TableName("event")
@Accessors(chain = true)
@Schema(name = "Event", description = "")
public class Event implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    @Schema(description = "活动ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属社团ID
     */
    @TableField("circle_id")
    @Schema(description = "所属社团ID")
    private Long circleId;

    /**
     * 创建者ID
     */
    @TableField("creator_id")
    @Schema(description = "创建者ID")
    private Long creatorId;

    /**
     * 活动标题
     */
    @TableField("title")
    @Schema(description = "活动标题")
    private String title;

    /**
     * 活动详情
     */
    @TableField("description")
    @Schema(description = "活动详情")
    private String description;

    /**
     * 活动地点
     */
    @TableField("location")
    @Schema(description = "活动地点")
    private String location;

    /**
     * 活动开始时间
     */
    @TableField("start_time")
    @Schema(description = "活动开始时间")
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    @TableField("end_time")
    @Schema(description = "活动结束时间")
    private LocalDateTime endTime;

    /**
     * 最大报名人数，NULL表示不限制
     */
    @TableField("max_capacity")
    @Schema(description = "最大报名人数，NULL表示不限制")
    private Integer maxCapacity;

    /**
     * 冗余字段：当前报名人数
     */
    @TableField("current_count")
    @Schema(description = "冗余字段：当前报名人数")
    private Integer currentCount;

    /**
     * 0仅限社团成员 1全校公开
     */
    @TableField("is_public")
    @Schema(description = "0仅限社团成员 1全校公开")
    private Integer isPublic;

    /**
     * 来源活动ID（复制时记录原活动）
     */
    @TableField("source_event_id")
    @Schema(description = "来源活动ID（复制时记录原活动）")
    private Long sourceEventId;

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
