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
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 部门管理
 * </p>
 *
 * @author lengleng
 * @since 2018-01-22
 */
@Data
@Schema(description = "部门")
@EqualsAndHashCode(callSuper = true)
public class SysDept extends TenantEntity {

	private static final long serialVersionUID = 1L;

	@TableId(value = "dept_id", type = IdType.ASSIGN_ID)
	@Schema(description = "部门id")
	private Long deptId;

	/**
	 * 部门名称
	 */
	@NotBlank(message = "部门名称不能为空")
	@Schema(description = "部门名称")
	private String name;

	/**
	 * 排序
	 */
	@NotNull(message = "排序值不能为空")
	@Schema(description = "排序值")
	private Integer sortOrder;

	/**
	 * 父级部门id
	 */
	@Schema(description = "父级部门id")
	private Long parentId;

}
