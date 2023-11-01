package com.skyler.cloud.skyler.admin.api.convert.tenant;

import com.skyler.cloud.skyler.admin.api.dto.tenant.TenantPackageCreateDTO;
import com.skyler.cloud.skyler.admin.api.dto.tenant.TenantPackageUpdateDTO;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantPackageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 租户套餐 实体转换工具
 * @author lizuoyang
 * @date 2023/10/31
 */
@Mapper
public interface TenantPackageConvert {
	TenantPackageConvert INSTANCE = Mappers.getMapper(TenantPackageConvert.class);

	SysTenantPackageEntity convert (TenantPackageCreateDTO tenantPackageDTO);

	SysTenantPackageEntity convert (TenantPackageUpdateDTO tenantPackageDTO);
}
