package com.skyler.cloud.skyler.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 租户套餐表
 *
 * @author skyler
 * @date 2023-10-26 14:09:01
 */
@Data
@TableName("sys_tenant_package")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "租户套餐表")
public class SysTenantPackageEntity extends Model<SysTenantPackageEntity> {


	/**
	* 套餐编号
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="套餐编号")
    private Long id;

	/**
	* 套餐名
	*/
    @Schema(description="套餐名")
    private String name;

	/**
	* 租户状态
	*/
    @Schema(description="租户状态")
    private Integer status;

	/**
	* 备注
	*/
    @Schema(description="备注")
    private String remark;

    /**
	* 关联的菜单编号
	*/
    @Schema(description="关联的菜单编号")
    private String menuIds;

	/**
	* 创建人
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建人")
    private String createBy;

	/**
	* 创建时间
	*/
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="创建时间")
    private LocalDateTime createTime;

	/**
	* 修改人
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="修改人")
    private String updateBy;

	/**
	* 更新时间
	*/
	@TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description="更新时间")
    private LocalDateTime updateTime;

	/**
	* 删除标记，0未删除，1已删除
	*/
    @TableLogic
	@TableField(fill = FieldFill.INSERT)
    @Schema(description="删除标记，0未删除，1已删除")
    private String delFlag;
}
