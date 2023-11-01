package com.skyler.cloud.skyler.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skyler.cloud.skyler.admin.api.dto.tenant.TenantCreateDTO;
import com.skyler.cloud.skyler.admin.api.dto.tenant.TenantUpdateDTO;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantEntity;

public interface SysTenantService extends IService<SysTenantEntity> {

	/**
	 * 创建租户
	 * @param tenantDTO
	 * @return {@link Long}
	 */
	Long createTenant(TenantCreateDTO tenantDTO);


	/**
	 * 修改租户
	 * @param tenantUpdateDTO
	 * @return
	 */
	Long updateTenant(TenantUpdateDTO tenantUpdateDTO);

	/**
	 * 更新租户的角色菜单
	 *
	 * @param tenantId 租户编号
	 * @param menuIds 菜单编号数组
	 */
	void updateTenantRoleMenu(Long tenantId, String menuIds);
}
