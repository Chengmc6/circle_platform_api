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
@TableName("circle_post_comment")
@Schema(name = "CirclePostComment", description = "")
public class CirclePostComment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    @Schema(description = "评论ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属主贴ID
     */
    @TableField("post_id")
    @Schema(description = "所属主贴ID")
    private Long postId;

    /**
     * 评论者ID
     */
    @TableField("user_id")
    @Schema(description = "评论者ID")
    private Long userId;

    /**
     * 评论内容
     */
    @TableField("content")
    @Schema(description = "评论内容")
    private String content;

    /**
     * 父评论ID，NULL表示一级评论，非NULL表示回复
     */
    @TableField("parent_id")
    @Schema(description = "父评论ID，NULL表示一级评论，非NULL表示回复")
    private Long parentId;

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
