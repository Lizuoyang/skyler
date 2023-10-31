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

package com.skyler.cloud.skyler.admin.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skyler.cloud.skyler.admin.api.dto.SysLogDTO;
import com.skyler.cloud.skyler.admin.api.entity.SysLog;
import com.skyler.cloud.skyler.admin.mapper.SysLogMapper;
import com.skyler.cloud.skyler.admin.service.SysLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2017-11-20
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

	@Override
	public Page getLogByPage(Page page, SysLogDTO sysLog) {

		LambdaQueryWrapper<SysLog> wrapper = Wrappers.lambdaQuery();
		if (StrUtil.isNotBlank(sysLog.getLogType())) {
			wrapper.eq(SysLog::getLogType, sysLog.getLogType());
		}

		if (ArrayUtil.isNotEmpty(sysLog.getCreateTime())) {
			wrapper.ge(SysLog::getCreateTime, sysLog.getCreateTime()[0])
				.le(SysLog::getCreateTime, sysLog.getCreateTime()[1]);
		}

		return baseMapper.selectPage(page, wrapper);
	}

	/**
	 * 插入日志
	 * @param sysLog 日志对象
	 * @return true/false
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean saveLog(SysLog sysLog) {
		baseMapper.insert(sysLog);
		return Boolean.TRUE;
	}

}
