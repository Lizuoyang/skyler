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

package com.skyler.cloud.skyler.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skyler.cloud.skyler.admin.api.entity.SysRoleMenu;

import java.util.Collection;
import java.util.Set;

import static java.util.Collections.singleton;

/**
 * <p>
 * 角色菜单表 服务类
 * </p>
 *
 * @author lengleng
 * @since 2017-10-29
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

	/**
	 * 更新角色菜单
	 * @param roleId 角色ID
	 * @param menuIds 菜单ID拼成的字符串，每个id之间根据逗号分隔
	 * @return
	 */
	Boolean saveRoleMenus(Long roleId, String menuIds);

	/**
	 * 获得角色拥有的菜单编号集合
	 *
	 * @param roleId 角色编号
	 * @return 菜单编号集合
	 */
	default Set<Long> getRoleMenuListByRoleId(Long roleId) {
		return getRoleMenuListByRoleId(singleton(roleId));
	}

	/**
	 * 获得角色们拥有的菜单编号集合
	 *
	 * @param roleIds 角色编号数组
	 * @return 菜单编号集合
	 */
	Set<Long> getRoleMenuListByRoleId(Collection<Long> roleIds);

}
