package com.example.circle_platform_api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("user_oauth")
@Schema(name = "UserOauth", description = "")
public class UserOauth implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联用户ID
     */
    @TableField("user_id")
    @Schema(description = "关联用户ID")
    private Long userId;

    /**
     * 第三方平台（GitHub/google/apple/line/WeChat）
     */
    @TableField("provider")
    @Schema(description = "第三方平台（github/google/apple/line/wechat）")
    private String provider;

    /**
     * 第三方平台用户唯一标识
     */
    @TableField("provider_account_id")
    @Schema(description = "第三方平台用户唯一标识")
    private String providerAccountId;

    /**
     * 第三方邮箱（可能为空）
     */
    @TableField("email")
    @Schema(description = "第三方邮箱（可能为空）")
    private String email;

    /**
     * 第三方昵称
     */
    @TableField("nickname")
    @Schema(description = "第三方昵称")
    private String nickname;

    /**
     * 第三方头像
     */
    @TableField("avatar")
    @Schema(description = "第三方头像")
    private String avatar;

    /**
     * Access Token
     */
    @TableField("access_token")
    @Schema(description = "Access Token")
    private String accessToken;

    /**
     * Refresh Token
     */
    @TableField("refresh_token")
    @Schema(description = "Refresh Token")
    private String refreshToken;

    /**
     * Token过期时间
     */
    @TableField("expired_at")
    @Schema(description = "Token过期时间")
    private LocalDateTime expiredAt;

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
}
