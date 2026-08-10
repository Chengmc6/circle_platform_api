package com.example.circle_platform_api.entity;

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
@TableName("category")
@Accessors(chain = true)
@Schema(name = "Category", description = "")
public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @Schema(description = "分类ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分类名称
     */
    @TableField("name")
    @Schema(description = "分类名称")
    private String name;

    /**
     * 0社团分类 1设施分类
     */
    @TableField("type")
    @Schema(description = "0社团分类 1设施分类")
    private Integer type;

    /**
     * 排序
     */
    @TableField("sort_order")
    @Schema(description = "排序")
    private Integer sortOrder;

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
    @TableLogic(value = "null",delval = "now()")
    @TableField("delete_at")
    @Schema(description = "删除时间（NULL：未删除；非NULL：已逻辑删除）")
    private LocalDateTime deleteAt;
}
