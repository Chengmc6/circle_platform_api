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
@TableName("facility")
@Accessors(chain = true)
@Schema(name = "Facility", description = "")
public class Facility implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 设施ID
     */
    @Schema(description = "设施ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设施名称
     */
    @TableField("name")
    @Schema(description = "设施名称")
    private String name;

    /**
     * 设施位置
     */
    @TableField("location")
    @Schema(description = "设施位置")
    private String location;

    /**
     * 容纳人数
     */
    @TableField("capacity")
    @Schema(description = "容纳人数")
    private Integer capacity;

    /**
     * 分类ID
     */
    @TableField("category_id")
    @Schema(description = "分类ID")
    private Long categoryId;

    /**
     * 设施说明
     */
    @TableField("description")
    @Schema(description = "设施说明")
    private String description;

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
