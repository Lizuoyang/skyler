package com.skyler.cloud.skyler.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.skyler.cloud.skyler.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class SysTenantPackageEntity extends BaseEntity {


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

}
