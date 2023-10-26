package com.skyler.cloud.skyler.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户套餐菜单表
 *
 * @author skyler
 * @date 2023-10-26 16:17:34
 */
@Data
@TableName("sys_tenant_package_menu")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "租户套餐菜单表")
public class SysTenantPackageMenuEntity extends Model<SysTenantPackageMenuEntity> {


	/**
	* 租户套餐ID
	*/
    @Schema(description="租户套餐ID")
    private Long tenantPackageId;

	/**
	* 菜单ID
	*/
    @Schema(description="菜单ID")
    private Long menuId;
}
