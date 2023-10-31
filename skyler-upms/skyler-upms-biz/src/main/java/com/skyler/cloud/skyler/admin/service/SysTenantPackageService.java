package com.skyler.cloud.skyler.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skyler.cloud.skyler.admin.api.dto.tenant.TenantPackageDTO;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantPackageEntity;

public interface SysTenantPackageService extends IService<SysTenantPackageEntity> {

	/**
	 * 创建租户套餐
	 * @param tenantPackageDTO
	 * @return boolean
	 */
	boolean saveTenantMenu(TenantPackageDTO tenantPackageDTO);

	/**
	 * 校验租户套餐是否存在
	 * @param id
	 * @return {@link SysTenantPackageEntity}
	 */
	SysTenantPackageEntity validTenantPackage(Long id);
}
