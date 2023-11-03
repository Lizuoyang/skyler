package com.skyler.cloud.skyler.common.security.service;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.skyler.cloud.skyler.admin.api.dto.UserInfo;
import com.skyler.cloud.skyler.admin.api.entity.SysUser;
import com.skyler.cloud.skyler.common.core.beans.SkylerUser;
import com.skyler.cloud.skyler.common.core.constant.CommonConstants;
import com.skyler.cloud.skyler.common.core.constant.SecurityConstants;
import com.skyler.cloud.skyler.common.core.util.R;
import com.skyler.cloud.skyler.common.core.util.RetOps;
import org.springframework.core.Ordered;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lengleng
 * @date 2021/12/21
 */
public interface SkylerUserDetailsService extends UserDetailsService, Ordered {

	/**
	 * 是否支持此客户端校验
	 * @param clientId 目标客户端
	 * @return true/false
	 */
	default boolean support(String clientId, String grantType) {
		return true;
	}

	/**
	 * 排序值 默认取最大的
	 * @return 排序值
	 */
	default int getOrder() {
		return 0;
	}

	/**
	 * 构建userdetails
	 * @param result 用户信息
	 * @return UserDetails
	 */
	default UserDetails getUserDetails(R<UserInfo> result) {
		UserInfo info = RetOps.of(result).getData().orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

		Set<String> dbAuthsSet = new HashSet<>();

		if (ArrayUtil.isNotEmpty(info.getRoles())) {
			// 获取角色
			Arrays.stream(info.getRoles()).forEach(role -> dbAuthsSet.add(SecurityConstants.ROLE + role));
			// 获取资源
			dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));

		}

		Collection<GrantedAuthority> authorities = AuthorityUtils
			.createAuthorityList(dbAuthsSet.toArray(new String[0]));
		SysUser user = info.getSysUser();

		// 构造security用户
		return new SkylerUser(user.getUserId(), user.getDeptId(), user.getUsername(),
				SecurityConstants.BCRYPT + user.getPassword(), user.getPhone(), info.getRoles(), info.getRoleDataScopes(), user.getTenantId(), true, true, true,
				StrUtil.equals(user.getLockFlag(), CommonConstants.STATUS_NORMAL), authorities);
	}

	/**
	 * 通过用户实体查询
	 * @param skylerUser user
	 * @return
	 */
	default UserDetails loadUserByUser(SkylerUser skylerUser) {
		return this.loadUserByUsername(skylerUser.getUsername());
	}

}
