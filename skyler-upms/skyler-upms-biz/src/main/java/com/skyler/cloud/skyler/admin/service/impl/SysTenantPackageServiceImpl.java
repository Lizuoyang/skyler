package com.skyler.cloud.skyler.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skyler.cloud.skyler.admin.api.convert.tenant.TenantPackageConvert;
import com.skyler.cloud.skyler.admin.api.dto.TenantPackageDTO;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantPackageEntity;
import com.skyler.cloud.skyler.admin.mapper.SysTenantPackageMapper;
import com.skyler.cloud.skyler.admin.service.SysTenantPackageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 租户套餐表
 *
 * @author skyler
 * @date 2023-10-26 14:09:01
 */
@Service
@AllArgsConstructor
public class SysTenantPackageServiceImpl extends ServiceImpl<SysTenantPackageMapper, SysTenantPackageEntity> implements SysTenantPackageService {

	@Override
	public boolean saveTenantMenu(TenantPackageDTO tenantPackageDTO) {
		SysTenantPackageEntity saveEntity = TenantPackageConvert.INSTANCE.convert(tenantPackageDTO);
		return this.save(saveEntity);
	}
}
