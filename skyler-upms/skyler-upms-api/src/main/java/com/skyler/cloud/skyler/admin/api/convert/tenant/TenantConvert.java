package com.skyler.cloud.skyler.admin.api.convert.tenant;

import com.skyler.cloud.skyler.admin.api.dto.tenant.TenantDTO;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * 租户 实体转换工具
 * </p>
 *
 * @author lzy
 * @since 2023/10/31 16:14
 */
@Mapper
public interface TenantConvert {
	TenantConvert INSTANCE = Mappers.getMapper(TenantConvert.class);

	SysTenantEntity covert(TenantDTO tenantDTO);
}