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
@TableName("notify")
@Accessors(chain = true)
@Schema(name = "Notify", description = "")
public class Notify implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 公告ID
     */
    @Schema(description = "公告ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属社团ID（NULL表示全校公告）
     */
    @TableField("circle_id")
    @Schema(description = "所属社团ID（NULL表示全校公告）")
    private Long circleId;

    /**
     * 发布者ID
     */
    @TableField("user_id")
    @Schema(description = "发布者ID")
    private Long userId;

    /**
     * 公告标题
     */
    @TableField("title")
    @Schema(description = "公告标题")
    private String title;

    /**
     * 公告内容
     */
    @TableField("content")
    @Schema(description = "公告内容")
    private String content;

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
