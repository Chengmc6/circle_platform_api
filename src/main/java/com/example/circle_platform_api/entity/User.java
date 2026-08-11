package com.example.circle_platform_api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.circle_platform_api.enums.UserRole;
import com.example.circle_platform_api.enums.UserStatus;
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
@TableName("user")
@Accessors(chain = true)
@Schema(name = "User", description = "")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField("username")
    @Schema(description = "用户名")
    private String username;

    /**
     * 密码（加密）
     */
    @JsonIgnore
    @TableField("password")
    @Schema(description = "密码（加密）")
    private String password;

    /**
     * 邮箱
     */
    @TableField("email")
    @Schema(description = "邮箱")
    private String email;

    /**
     * 昵称
     */
    @TableField("nickname")
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 头像URL
     */
    @TableField("avatar")
    @Schema(description = "头像URL")
    private String avatar;

    /**
     * 0普通用户 1系统管理员
     */
    @TableField("role")
    @Schema(description = "0普通用户 1系统管理员")
    private UserRole role;

    /**
     * 0在籍 1毕业/休学
     */
    @TableField("status")
    @Schema(description = "0在籍 1毕业/休学")
    private UserStatus status;

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
