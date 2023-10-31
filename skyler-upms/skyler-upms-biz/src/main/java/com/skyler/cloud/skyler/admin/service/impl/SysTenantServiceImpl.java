package com.skyler.cloud.skyler.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skyler.cloud.skyler.admin.api.convert.tenant.TenantConvert;
import com.skyler.cloud.skyler.admin.api.dto.tenant.TenantDTO;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantEntity;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantPackageEntity;
import com.skyler.cloud.skyler.admin.mapper.SysTenantMapper;
import com.skyler.cloud.skyler.admin.service.SysTenantPackageService;
import com.skyler.cloud.skyler.admin.service.SysTenantService;
import com.skyler.cloud.skyler.common.core.exception.enums.ErrorCodeEnum;
import com.skyler.cloud.skyler.common.core.exception.utils.ServiceExceptionUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
/**
 * 租户表
 *
 * @author skyler
 * @date 2023-10-24 11:44:11
 */
@Service
@AllArgsConstructor
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenantEntity> implements SysTenantService {
	private final SysTenantPackageService tenantPackageService;
	@Override
	public Long createTenant(TenantDTO tenantDTO) {
		// 校验租户名称是否重复
		validTenantNameDuplicate(tenantDTO.getName(), null);
		// 校验套餐被禁用
		SysTenantPackageEntity tenantPackage = tenantPackageService.validTenantPackage(tenantDTO.getPackageId());

		// 创建租户
		SysTenantEntity tenantEntity = TenantConvert.INSTANCE.covert(tenantDTO);
		this.save(tenantEntity);

		// 创建租户管理员账户、分配角色
		return null;
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
}
