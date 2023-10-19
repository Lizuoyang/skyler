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

package com.skyler.cloud.skyler.common.core.beans;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lengleng
 * @date 2019/2/1 扩展用户信息
 */
public class SkylerUser extends User implements OAuth2AuthenticatedPrincipal {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private final Map<String, Object> attributes = new HashMap<>();

	/**
	 * 用户ID
	 */
	@Getter
	@JsonSerialize(using = ToStringSerializer.class)
	private final Long id;

	/**
	 * 部门ID
	 */
	@Getter
	@JsonSerialize(using = ToStringSerializer.class)
	private final Long deptId;

	/**
	 * 手机号
	 */
	@Getter
	private final String phone;

	/**
	 * 角色集合
	 */
	@Getter
	@JsonSerialize(using = ToStringSerializer.class)
	private Long[] roles;

	/**
	 * 角色数据权限集合
	 */
	@Getter
	private String[] roleDataScopes;

	public SkylerUser(Long id, Long deptId, String username, String password, String phone, Long[] roles, String[] roleDataScopes, boolean enabled,
                      boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                      Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.id = id;
		this.deptId = deptId;
		this.phone = phone;
		this.roles = roles;
		this.roleDataScopes = roleDataScopes;
	}

	/**
	 * Get the OAuth 2.0 token attributes
	 * @return the OAuth 2.0 token attributes
	 */
	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	@Override
	public String getName() {
		return this.getUsername();
	}

}
