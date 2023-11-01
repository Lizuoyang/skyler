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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.annotations.VisibleForTesting;
import com.pig4cloud.plugin.excel.vo.ErrorMessage;
import com.skyler.cloud.skyler.admin.api.convert.role.RoleConvert;
import com.skyler.cloud.skyler.admin.api.dto.role.RoleCreateDTO;
import com.skyler.cloud.skyler.admin.api.entity.SysRole;
import com.skyler.cloud.skyler.admin.api.entity.SysRoleMenu;
import com.skyler.cloud.skyler.admin.api.enums.RoleCodeEnum;
import com.skyler.cloud.skyler.admin.api.vo.RoleExcelVO;
import com.skyler.cloud.skyler.admin.api.vo.RoleVO;
import com.skyler.cloud.skyler.admin.mapper.SysRoleMapper;
import com.skyler.cloud.skyler.admin.service.SysRoleMenuService;
import com.skyler.cloud.skyler.admin.service.SysRoleService;
import com.skyler.cloud.skyler.common.core.constant.CacheConstants;
import com.skyler.cloud.skyler.common.core.constant.CommonConstants;
import com.skyler.cloud.skyler.common.core.exception.ErrorCodes;
import com.skyler.cloud.skyler.common.core.exception.enums.ErrorCodeEnum;
import com.skyler.cloud.skyler.common.core.exception.utils.ServiceExceptionUtil;
import com.skyler.cloud.skyler.common.core.util.MsgUtils;
import com.skyler.cloud.skyler.common.core.util.R;
import com.skyler.cloud.skyler.common.datapermission.enums.DataScopeType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2017-10-29
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

	private SysRoleMenuService roleMenuService;

	@Autowired
	public void setSysRoleMenuService(@Lazy SysRoleMenuService roleMenuService) {
		this.roleMenuService = roleMenuService;
	}
	/**
	 * 通过用户ID，查询角色信息
	 * @param userId
	 * @return
	 */
	@Override
	public List findRolesByUserId(Long userId) {
		return baseMapper.listRolesByUserId(userId);
	}

	/**
	 * 根据角色ID 查询角色列表，注意缓存删除
	 * @param roleIdList 角色ID列表
	 * @param key 缓存key
	 * @return
	 */
	@Override
	@Cacheable(value = CacheConstants.ROLE_DETAILS, key = "#key", unless = "#result.isEmpty()")
	public List<SysRole> findRolesByRoleIds(List<Long> roleIdList, String key) {
		return baseMapper.selectBatchIds(roleIdList);
	}

	@Override
	public boolean hasAnySuperAdmin(Collection<Long> ids) {
		if (CollectionUtil.isEmpty(ids)) {
			return false;
		}
		SysRoleServiceImpl self = getSelf();
		return ids.stream().anyMatch(id -> {
			SysRole role = self.getRoleFromCache(id);
			return role != null && RoleCodeEnum.isSuperAdmin(role.getRoleCode());
		});
	}

	@Override
	@Cacheable(value = CacheConstants.ROLE, key = "#id",
			unless = "#result.isEmpty()")
	public SysRole getRoleFromCache(Long id) {
		return this.getById(id);
	}

	/**
	 * 通过角色ID，删除角色,并清空角色菜单缓存
	 * @param ids
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeRoleByIds(Long[] ids) {
		roleMenuService
			.remove(Wrappers.<SysRoleMenu>update().lambda().in(SysRoleMenu::getRoleId, CollUtil.toList(ids)));
		return this.removeBatchByIds(CollUtil.toList(ids));
	}

	@Override
	public Long createRole(RoleCreateDTO roleCreateDTO) {
		// 校验角色
		validateRoleDuplicate(roleCreateDTO.getRoleName(), roleCreateDTO.getRoleCode(), null);
		SysRole role = RoleConvert.INSTANCE.convert(roleCreateDTO);
		role.setDelFlag(CommonConstants.STATUS_NORMAL);
		// 默认可查看所有数据。原因是，可能一些项目不需要项目权限
		role.setDataScope(DataScopeType.ALL.getCode());
		this.save(role);
		return role.getRoleId();
	}

	/**
	 * 根据角色菜单列表
	 * @param roleVo 角色&菜单列表
	 * @return
	 */
	@Override
	public Boolean updateRoleMenus(RoleVO roleVo) {
		return roleMenuService.saveRoleMenus(roleVo.getRoleId(), roleVo.getMenuIds());
	}

	/**
	 * 导入角色
	 * @param excelVOList 角色列表
	 * @param bindingResult 错误信息列表
	 * @return ok fail
	 */
	@Override
	public R importRole(List<RoleExcelVO> excelVOList, BindingResult bindingResult) {
		// 通用校验获取失败的数据
		List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

		// 个性化校验逻辑
		List<SysRole> roleList = this.list();

		// 执行数据插入操作 组装 RoleDto
		for (RoleExcelVO excel : excelVOList) {
			Set<String> errorMsg = new HashSet<>();
			// 检验角色名称或者角色编码是否存在
			boolean existRole = roleList.stream()
				.anyMatch(sysRole -> excel.getRoleName().equals(sysRole.getRoleName())
						|| excel.getRoleCode().equals(sysRole.getRoleCode()));

			if (existRole) {
				errorMsg.add(MsgUtils.getMessage(ErrorCodes.SYS_ROLE_NAMEORCODE_EXISTING, excel.getRoleName(),
						excel.getRoleCode()));
			}

			// 数据合法情况
			if (CollUtil.isEmpty(errorMsg)) {
				insertExcelRole(excel);
			}
			else {
				// 数据不合法情况
				errorMessageList.add(new ErrorMessage(excel.getLineNum(), errorMsg));
			}
		}
		if (CollUtil.isNotEmpty(errorMessageList)) {
			return R.failed(errorMessageList);
		}
		return R.ok();
	}

	/**
	 * 查询全部的角色
	 * @return list
	 */
	@Override
	public List<RoleExcelVO> listRole() {
		List<SysRole> roleList = this.list(Wrappers.emptyWrapper());
		// 转换成execl 对象输出
		return roleList.stream().map(role -> {
			RoleExcelVO roleExcelVO = new RoleExcelVO();
			BeanUtil.copyProperties(role, roleExcelVO);
			return roleExcelVO;
		}).collect(Collectors.toList());
	}

	/**
	 * 插入excel Role
	 */
	private void insertExcelRole(RoleExcelVO excel) {
		SysRole sysRole = new SysRole();
		sysRole.setRoleName(excel.getRoleName());
		sysRole.setRoleDesc(excel.getRoleDesc());
		sysRole.setRoleCode(excel.getRoleCode());
		this.save(sysRole);
	}

	/**
	 * 校验角色的唯一字段是否重复
	 *
	 * 1. 是否存在相同名字的角色
	 * 2. 是否存在相同编码的角色
	 *
	 * @param name 角色名字
	 * @param code 角色额编码
	 * @param id 角色编号
	 */
	@VisibleForTesting
	void validateRoleDuplicate(String name, String code, Long id) {
		// 0. 超级管理员，不允许创建
		if (RoleCodeEnum.isSuperAdmin(code)) {
			throw ServiceExceptionUtil.exception(ErrorCodeEnum.ROLE_ADMIN_CODE_ERROR, code);
		}
		// 1. 该 name 名字被其它角色所使用
		SysRole role = this.getOne(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getRoleName, name));
		if (role != null && !role.getRoleCode().equals(id)) {
			throw ServiceExceptionUtil.exception(ErrorCodeEnum.ROLE_NAME_DUPLICATE, name);
		}
		// 2. 是否存在相同编码的角色
		if (!StringUtils.hasText(code)) {
			return;
		}
		// 该 code 编码被其它角色所使用
		role = this.getOne(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getRoleCode, code));
		if (role != null && !role.getRoleId().equals(id)) {
			throw ServiceExceptionUtil.exception(ErrorCodeEnum.ROLE_CODE_DUPLICATE, code);
		}
	}

	/**
	 * 获得自身的代理对象，解决 AOP 生效问题
	 *
	 * @return 自己
	 */
	private SysRoleServiceImpl getSelf() {
		return SpringUtil.getBean(getClass());
	}

}
