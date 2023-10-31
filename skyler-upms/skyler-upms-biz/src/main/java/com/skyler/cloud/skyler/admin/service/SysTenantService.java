package com.skyler.cloud.skyler.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skyler.cloud.skyler.admin.api.dto.tenant.TenantDTO;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantEntity;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantPackageEntity;

public interface SysTenantService extends IService<SysTenantEntity> {

	/**
	 * 创建租户
	 * @param tenantDTO
	 * @return {@link Long}
	 */
	Long createTenant(TenantDTO tenantDTO);


}
