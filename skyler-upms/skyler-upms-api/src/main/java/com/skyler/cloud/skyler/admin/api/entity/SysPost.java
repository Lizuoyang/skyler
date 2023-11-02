/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the skyler4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.skyler.cloud.skyler.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.skyler.cloud.skyler.common.mybatis.base.TenantEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 岗位信息表
 *
 * @author fxz
 * @date 2022-03-26 12:50:43
 */
@Data
@TableName("sys_post")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "岗位信息表")
public class SysPost extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 岗位ID
	 */
	@TableId(value = "post_id", type = IdType.ASSIGN_ID)
	@Schema(description = "岗位ID")
	private Long postId;

	/**
	 * 岗位编码
	 */
	@NotBlank(message = "岗位编码不能为空")
	@Schema(description = "岗位编码")
	private String postCode;

	/**
	 * 岗位名称
	 */
	@NotBlank(message = "岗位名称不能为空")
	@Schema(description = "岗位名称")
	private String postName;

	/**
	 * 岗位排序
	 */
	@NotNull(message = "排序值不能为空")
	@Schema(description = "岗位排序")
	private Integer postSort;

	/**
	 * 岗位描述
	 */
	@Schema(description = "岗位描述")
	private String remark;

}
