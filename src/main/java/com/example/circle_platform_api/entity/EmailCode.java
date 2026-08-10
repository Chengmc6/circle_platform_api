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
@TableName("email_code")
@Schema(name = "EmailCode", description = "")
public class EmailCode implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 验证码ID
     */
    @Schema(description = "验证码ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 邮箱
     */
    @TableField("email")
    @Schema(description = "邮箱")
    private String email;

    /**
     * 验证码
     */
    @TableField("code")
    @Schema(description = "验证码")
    private String code;

    /**
     * 0忘记密码 1其他用途
     */
    @TableField("type")
    @Schema(description = "0忘记密码 1其他用途")
    private Integer type;

    /**
     * 过期时间
     */
    @TableField("expired_at")
    @Schema(description = "过期时间")
    private LocalDateTime expiredAt;

    /**
     * 0未使用 1已使用
     */
    @TableField("is_used")
    @Schema(description = "0未使用 1已使用")
    private Integer isUsed;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(value = "created_at")
    private LocalDateTime createdAt;
}
