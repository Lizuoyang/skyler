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
 * 文件管理
 *
 * @author Luckly
 * @date 2019-06-18 17:18:42
 */
@Data
@Schema(description = "文件")
@EqualsAndHashCode(callSuper = true)
public class SysFile extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "文件编号")
	private Long id;

	/**
	 * 文件名
	 */
	@Schema(description = "文件名")
	private String fileName;

	/**
	 * 原文件名
	 */
	@Schema(description = "原始文件名")
	private String original;

	/**
	 * 容器名称
	 */
	@Schema(description = "存储桶名称")
	private String bucketName;

	/**
	 * 文件类型
	 */
	@Schema(description = "文件类型")
	private String type;

	/**
	 * 文件大小
	 */
	@Schema(description = "文件大小")
	private Long fileSize;

}
