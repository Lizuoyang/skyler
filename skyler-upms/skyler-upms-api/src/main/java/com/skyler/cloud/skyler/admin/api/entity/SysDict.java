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
import com.skyler.cloud.skyler.common.mybatis.base.TenantEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典表
 *
 * @author lengleng
 * @date 2019/03/19
 */
@Data
@Schema(description = "字典类型")
@EqualsAndHashCode(callSuper = true)
public class SysDict extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "字典编号")
	private Long id;

	/**
	 * 类型
	 */
	@Schema(description = "字典类型")
	private String dictType;

	/**
	 * 描述
	 */
	@Schema(description = "字典描述")
	private String description;

	/**
	 * 是否是系统内置
	 */
	@Schema(description = "是否系统内置")
	private String systemFlag;

	/**
	 * 备注信息
	 */
	@Schema(description = "备注信息")
	private String remarks;

}
