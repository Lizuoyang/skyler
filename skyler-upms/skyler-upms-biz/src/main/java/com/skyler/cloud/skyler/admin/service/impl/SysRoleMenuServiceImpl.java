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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skyler.cloud.skyler.admin.api.entity.SysMenu;
import com.skyler.cloud.skyler.admin.api.entity.SysRoleMenu;
import com.skyler.cloud.skyler.admin.mapper.SysRoleMenuMapper;
import com.skyler.cloud.skyler.admin.service.SysMenuService;
import com.skyler.cloud.skyler.admin.service.SysRoleMenuService;
import com.skyler.cloud.skyler.admin.service.SysRoleService;
import com.skyler.cloud.skyler.common.core.constant.CacheConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.skyler.cloud.skyler.common.core.exception.utils.CollectionUtils.convertSet;

/**
 * <p>
 * 角色菜单表 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2017-10-29
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

	private final CacheManager cacheManager;
	private final SysMenuService menuService;

	private SysRoleService roleService;

	@Autowired
	public void setSysRoleService(@Lazy SysRoleService roleService) {
		this.roleService = roleService;
	}
	/**
	 * @param roleId 角色
	 * @param menuIds 菜单ID拼成的字符串，每个id之间根据逗号分隔
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.MENU_DETAILS, key = "#roleId")
	public Boolean saveRoleMenus(Long roleId, String menuIds) {
		this.remove(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getRoleId, roleId));

		if (StrUtil.isBlank(menuIds)) {
			return Boolean.TRUE;
		}
		List<SysRoleMenu> roleMenuList = Arrays.stream(menuIds.split(StrUtil.COMMA)).map(menuId -> {
			SysRoleMenu roleMenu = new SysRoleMenu();
			roleMenu.setRoleId(roleId);
			roleMenu.setMenuId(Long.valueOf(menuId));
			return roleMenu;
		}).collect(Collectors.toList());

		// 清空userinfo
		cacheManager.getCache(CacheConstants.USER_DETAILS).clear();
		this.saveBatch(roleMenuList);
		return Boolean.TRUE;
	}

	@Override
	public Set<Long> getRoleMenuListByRoleId(Collection<Long> roleIds) {
		if (CollUtil.isEmpty(roleIds)) {
			return Collections.emptySet();
		}

		// 如果是管理员的情况下，获取全部菜单编号
		if (roleService.hasAnySuperAdmin(roleIds)) {
			return convertSet(menuService.list(), SysMenu::getMenuId);
		}
		// 如果是非管理员的情况下，获得拥有的菜单编号
		return convertSet(this.list(Wrappers.<SysRoleMenu>lambdaQuery().in(SysRoleMenu::getRoleId, roleIds)), SysRoleMenu::getMenuId);
	}

}
