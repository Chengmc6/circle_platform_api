package com.example.circle_platform_api.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("circle_member")
@Schema(name = "CircleMember", description = "")
public class CircleMember implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 成员ID
     */
    @Schema(description = "成员ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 社团ID
     */
    @TableField("circle_id")
    @Schema(description = "社团ID")
    private Long circleId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 0普通成员 1副社长(可多个) 2社长(唯一)
     */
    @TableField("role")
    @Schema(description = "0普通成员 1副社长(可多个) 2社长(唯一)")
    private Integer role;

    /**
     * 加入时间
     */
    @TableField("joined_at")
    @Schema(description = "加入时间")
    private LocalDateTime joinedAt;

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
    @TableLogic(value = "null", delval = "now()")
    @TableField("delete_at")
    @Schema(description = "删除时间（NULL：未删除；非NULL：已逻辑删除）")
    private LocalDateTime deleteAt;
}
