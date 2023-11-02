/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the skyler4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.skyler.cloud.skyler.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.skyler.cloud.skyler.common.mybatis.base.TenantEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author lengleng
 * @since 2017-10-29
 */
@Data
@Schema(description = "角色")
@EqualsAndHashCode(callSuper = true)
public class SysRole extends TenantEntity {

	private static final long serialVersionUID = 1L;

	@TableId(value = "role_id", type = IdType.ASSIGN_ID)
	@Schema(description = "角色编号")
	private Long roleId;

	@NotBlank(message = "角色名称不能为空")
	@Schema(description = "角色名称")
	private String roleName;

	@NotBlank(message = "角色标识不能为空")
	@Schema(description = "角色标识")
	private String roleCode;

	@Schema(description = "角色描述")
	private String roleDesc;

	@Schema(description = "数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）")
	private String dataScope;

}
