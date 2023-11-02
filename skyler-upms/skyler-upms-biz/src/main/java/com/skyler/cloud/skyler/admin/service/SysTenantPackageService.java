package com.skyler.cloud.skyler.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skyler.cloud.skyler.admin.api.dto.tenant.TenantPackageCreateDTO;
import com.skyler.cloud.skyler.admin.api.dto.tenant.TenantPackageUpdateDTO;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantPackageEntity;

public interface SysTenantPackageService extends IService<SysTenantPackageEntity> {

	/**
	 * 创建租户套餐
	 * @param createDTO
	 * @return boolean
	 */
	boolean saveTenantPackage(TenantPackageCreateDTO createDTO);

	/**
	 * 更新租户套餐
	 * @param updateDTO
	 */
	void updateTeantPackage(TenantPackageUpdateDTO updateDTO);

	/**
	 * 校验租户套餐是否存在
	 * @param id
	 * @return {@link SysTenantPackageEntity}
	 */
	SysTenantPackageEntity validTenantPackage(Long id);
}
