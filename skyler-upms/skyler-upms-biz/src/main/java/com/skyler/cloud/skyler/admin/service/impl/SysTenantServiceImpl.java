package com.skyler.cloud.skyler.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skyler.cloud.skyler.admin.api.convert.tenant.TenantConvert;
import com.skyler.cloud.skyler.admin.api.dto.role.RoleCreateDTO;
import com.skyler.cloud.skyler.admin.api.dto.tenant.TenantCreateDTO;
import com.skyler.cloud.skyler.admin.api.dto.tenant.TenantUpdateDTO;
import com.skyler.cloud.skyler.admin.api.entity.SysRole;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantEntity;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantPackageEntity;
import com.skyler.cloud.skyler.admin.api.enums.RoleCodeEnum;
import com.skyler.cloud.skyler.admin.mapper.SysTenantMapper;
import com.skyler.cloud.skyler.admin.service.SysRoleMenuService;
import com.skyler.cloud.skyler.admin.service.SysRoleService;
import com.skyler.cloud.skyler.admin.service.SysTenantPackageService;
import com.skyler.cloud.skyler.admin.service.SysTenantService;
import com.skyler.cloud.skyler.admin.service.SysUserService;
import com.skyler.cloud.skyler.common.core.exception.enums.ErrorCodeEnum;
import com.skyler.cloud.skyler.common.core.exception.utils.ServiceExceptionUtil;
import com.skyler.cloud.skyler.common.tenant.utils.TenantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 租户表
 *
 * @author skyler
 * @date 2023-10-24 11:44:11
 */
@Service
@AllArgsConstructor
@Slf4j
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenantEntity> implements SysTenantService {
	private final SysTenantPackageService tenantPackageService;
	private final SysRoleMenuService roleMenuService;
	private final SysRoleService roleService;
	private final SysUserService userService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long createTenant(TenantCreateDTO tenantDTO) {
		// 校验租户名称是否重复
		validTenantNameDuplicate(tenantDTO.getName(), null);
		// 校验套餐被禁用
		SysTenantPackageEntity tenantPackage = tenantPackageService.validTenantPackage(tenantDTO.getPackageId());

		// 创建租户
		SysTenantEntity tenantEntity = TenantConvert.INSTANCE.covert(tenantDTO);
		this.save(tenantEntity);

		// 创建租户管理员账户、分配角色
		TenantUtils.execute(tenantEntity.getId(), () -> {
			// 创建角色
			Long roleId = createRole(tenantPackage);
			// 创建用户，并分配角色
			Long userId = createUser(roleId, tenantDTO);
			// 修改租户的管理员
			SysTenantEntity updateTenantEntity = new SysTenantEntity();
			updateTenantEntity.setId(tenantEntity.getId());
			updateTenantEntity.setContactUserId(userId);
			this.updateById(updateTenantEntity);
		});
		return null;
	}

	@Override
	public Long updateTenant(TenantUpdateDTO tenantUpdateDTO) {
		// 校验存在
		validateUpdateTenant(tenantUpdateDTO.getId());
		// 校验租户名称是否重复
		validTenantNameDuplicate(tenantUpdateDTO.getName(),tenantUpdateDTO.getId());
		// 校验套餐被禁用
		SysTenantPackageEntity tenantPackage = tenantPackageService.validTenantPackage(tenantUpdateDTO.getPackageId());

		// 更新租户
		SysTenantEntity updateEntity = TenantConvert.INSTANCE.covert(tenantUpdateDTO);
		this.updateById(updateEntity);

		// 如果套餐发生变化，则修改其角色的权限
		if (ObjUtil.notEqual(tenantUpdateDTO.getPackageId(), tenantPackage.getId())) {
			updateTenantRoleMenu(tenantUpdateDTO.getId(), tenantPackage.getMenuIds());
		}
		return updateEntity.getId();
	}

	@Override
	public void updateTenantRoleMenu(Long tenantId, String menuIds) {
		TenantUtils.execute(tenantId, () -> {
			// 获取所有的角色
			List<SysRole> roles = roleService.list();
			roles.forEach(role -> Assert.isTrue(ObjUtil.equal(tenantId, role.getTenantId()), "角色({}/{}) 租户不匹配", role.getRoleId(), role.getTenantId(), tenantId));
			// 重新分配每个角色的权限
			roles.forEach(role -> {
				// 如果是租户管理员，把选择的租户套餐分配给租户
				if (role.getRoleCode() == RoleCodeEnum.TENANT_ADMIN.getCode()) {
					roleMenuService.saveRoleMenus(role.getRoleId(), menuIds);
					log.info("[updateTenantRoleMenu][租户管理员({}/{}) 的权限修改为({})]", role.getRoleId(), role.getTenantId(), menuIds);
					return;
				}

				// 如果是其他角色，则去掉超过套餐的权限
				Set<Long> roleMenuIds = roleMenuService.getRoleMenuListByRoleId(role.getRoleId());
				Set<Long> menuIdSet = Convert.toSet(Long.class, menuIds);
				roleMenuIds = CollUtil.intersectionDistinct(roleMenuIds, menuIdSet);
				roleMenuService.saveRoleMenus(role.getRoleId(), CollUtil.join(roleMenuIds, StrUtil.COMMA));
				log.info("[updateTenantRoleMenu][角色({}/{}) 的权限修改为({})]", role.getRoleId(), role.getTenantId(), roleMenuIds);
			});
		});
	}

	private Long createRole(SysTenantPackageEntity tenantPackage) {
		RoleCreateDTO roleCreateDTO = new RoleCreateDTO();
		roleCreateDTO.setRoleName(RoleCodeEnum.TENANT_ADMIN.getName());
		roleCreateDTO.setRoleCode(RoleCodeEnum.TENANT_ADMIN.getCode());
		roleCreateDTO.setRoleDesc("系统自动生成");
		Long roleId = roleService.createRole(roleCreateDTO);
		// 分配权限
		roleMenuService.saveRoleMenus(roleId, tenantPackage.getMenuIds());
		return roleId;
	}

	private Long createUser(Long roleId, TenantCreateDTO tenantDTO) {
		Long userId = userService.saveUser(TenantConvert.INSTANCE.convert02(tenantDTO));
		return userId;
	}

	private SysTenantEntity validateUpdateTenant(Long id) {
		SysTenantEntity tenant = this.getById(id);
		if (tenant == null) {
			throw ServiceExceptionUtil.exception(ErrorCodeEnum.TENANT_NOT_EXISTS);
		}
		// 内置租户，不允许删除
		if (isSystemTenant(tenant)) {
			throw ServiceExceptionUtil.exception(ErrorCodeEnum.TENANT_CAN_NOT_UPDATE_SYSTEM);
		}
		return tenant;
	}

	private void validTenantNameDuplicate(String name, Long id) {
		SysTenantEntity tenant = this.getOne(Wrappers.<SysTenantEntity>lambdaQuery().eq(SysTenantEntity::getName, name));
		if (tenant == null) {
			return;
		}
		// 如果 id 为空，说明不用比较是否为相同名字的租户
		if (id == null) {
			throw ServiceExceptionUtil.exception(ErrorCodeEnum.TENANT_NAME_DUPLICATE,name);
		}
		if (!tenant.getId().equals(id)) {
			throw ServiceExceptionUtil.exception(ErrorCodeEnum.TENANT_NAME_DUPLICATE,name);
		}
	}

	private static boolean isSystemTenant(SysTenantEntity tenant) {
		return Objects.equals(tenant.getPackageId(), SysTenantEntity.PACKAGE_ID_SYSTEM);
	}
}
