package com.skyler.cloud.skyler.admin.conponents;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.skyler.cloud.skyler.admin.api.entity.SysDept;
import com.skyler.cloud.skyler.admin.api.entity.SysUser;
import com.skyler.cloud.skyler.admin.mapper.SysUserMapper;
import com.skyler.cloud.skyler.admin.service.SysDeptService;
import com.skyler.cloud.skyler.common.core.util.StreamUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 数据权限 实现
 * 注意: 此Service内不允许调用标注`数据权限`注解的方法
 * 例如: deptMapper.selectList 此 selectList 方法标注了`数据权限`注解 会出现循环解析的问题
 * </p>
 *
 * @author lzy
 * @since 2023/10/20 11:25
 */
@RequiredArgsConstructor
@Component("sdss")
public class SysDataScopeService {
	private final SysUserMapper userMapper;
	private final SysDeptService deptService;

	/**
	 * 获取角色自定义权限语句
	 * @param userId
	 * @return
	 */
	public String getUserCustom(Long userId) {
		List<SysUser> list = userMapper.selectList(
				Wrappers.<SysUser>lambdaQuery()
						.select(SysUser::getDeptId)
						.eq(SysUser::getUserId, userId));
		if (CollUtil.isNotEmpty(list)) {
			return StreamUtils.join(list, rd -> Convert.toStr(rd.getDeptId()));
		}
		return null;
	}

	/**
	 * 获取部门和下级权限语句
	 * @param deptId
	 * @return
	 */
	public String getDeptAndChild(Long deptId) {
		List<SysDept> deptList = deptService.listDescendant(deptId);

		List<Long> ids = StreamUtils.toList(deptList, SysDept::getDeptId);
		ids.add(deptId);
		List<SysDept> list = deptService.list(new LambdaQueryWrapper<SysDept>()
				.select(SysDept::getDeptId)
				.in(SysDept::getDeptId, ids));
		if (CollUtil.isNotEmpty(list)) {
			return StreamUtils.join(list, d -> Convert.toStr(d.getDeptId()));
		}
		return null;
	}
}
