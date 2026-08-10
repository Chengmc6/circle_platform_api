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
@TableName("circle")
@Accessors(chain = true)
@Schema(name = "Circle", description = "")
public class Circle implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 社团ID
     */
    @Schema(description = "社团ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 社团名称
     */
    @TableField("name")
    @Schema(description = "社团名称")
    private String name;

    /**
     * 社团简介
     */
    @TableField("description")
    @Schema(description = "社团简介")
    private String description;

    /**
     * 分类ID
     */
    @TableField("category_id")
    @Schema(description = "分类ID")
    private Long categoryId;

    /**
     * 社长ID（冗余字段，快速查询用）
     */
    @TableField("owner_id")
    @Schema(description = "社长ID（冗余字段，快速查询用）")
    private Long ownerId;

    /**
     * 0正常 1休眠(无管理员) 2已解散
     */
    @TableField("status")
    @Schema(description = "0正常 1休眠(无管理员) 2已解散")
    private Integer status;

    /**
     * 冗余字段：成员数
     */
    @TableField("member_count")
    @Schema(description = "冗余字段：成员数")
    private Integer memberCount;

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
