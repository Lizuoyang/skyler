/*
 * Copyright (c) 2020 skyler4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skyler.cloud.skyler.common.core.util;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import com.skyler.cloud.skyler.common.core.beans.SkylerUser;
import com.skyler.cloud.skyler.common.core.constant.SecurityConstants;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 安全工具类
 *
 * @author L.cm
 */
@UtilityClass
public class SecurityUtils {
	public static final String HEADER_TENANT_ID = "TENANT-ID";

	/**
	 * 获取Authentication
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获得租户编号，从 header 中
	 * 考虑到其它 framework 组件也会使用到租户编号，所以不得不放在 SecurityUtils 统一提供
	 *
	 * @param request 请求
	 * @return 租户编号
	 */
	public static Long getTenantId(HttpServletRequest request) {
		String tenantId = request.getHeader(HEADER_TENANT_ID);
		return NumberUtil.isNumber(tenantId) ? Long.valueOf(tenantId) : null;
	}


	/**
	 * 获取 HttpServletRequest
	 *
	 */
	public static HttpServletRequest getRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return (requestAttributes == null) ? null : ((ServletRequestAttributes) requestAttributes).getRequest();
	}

	/**
	 * 获取用户
	 */
	public SkylerUser getUser(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		if (principal instanceof SkylerUser) {
			return (SkylerUser) principal;
		}
		return null;
	}

	/**
	 * 获取用户
	 */
	public SkylerUser getUser() {
		Authentication authentication = getAuthentication();
		if (authentication == null) {
			return null;
		}
		return getUser(authentication);
	}

	/**
	 * 获取用户角色信息
	 * @return 角色集合
	 */
	public List<Long> getRoles() {
		Authentication authentication = getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		List<Long> roleIds = new ArrayList<>();
		authorities.stream()
			.filter(granted -> CharSequenceUtil.startWith(granted.getAuthority(), SecurityConstants.ROLE))
			.forEach(granted -> {
				String id = CharSequenceUtil.removePrefix(granted.getAuthority(), SecurityConstants.ROLE);
				roleIds.add(Long.parseLong(id));
			});
		return roleIds;
	}

	/**
	 * 是否为管理员
	 *
	 * @param userId 用户ID
	 * @return 结果
	 */
	public static boolean isAdmin(Long userId) {
		return SecurityConstants.ADMIN_ID.equals(userId);
	}

}
