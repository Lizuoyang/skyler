package com.skyler.cloud.skyler.admin.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skyler.cloud.skyler.admin.api.convert.tenant.TenantPackageConvert;
import com.skyler.cloud.skyler.admin.api.dto.tenant.TenantPackageCreateDTO;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantPackageEntity;
import com.skyler.cloud.skyler.admin.mapper.SysTenantPackageMapper;
import com.skyler.cloud.skyler.admin.service.SysTenantPackageService;
import com.skyler.cloud.skyler.common.core.constant.CommonConstants;
import com.skyler.cloud.skyler.common.core.exception.enums.ErrorCodeEnum;
import com.skyler.cloud.skyler.common.core.exception.utils.ServiceExceptionUtil;
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
	public boolean saveTenantMenu(TenantPackageCreateDTO tenantPackageDTO) {
		SysTenantPackageEntity saveEntity = TenantPackageConvert.INSTANCE.convert(tenantPackageDTO);
		return this.save(saveEntity);
	}

	@Override
	public SysTenantPackageEntity validTenantPackage(Long id) {
		SysTenantPackageEntity sysTenantPackageEntity = baseMapper.selectById(id);
		if (ObjUtil.isNull(sysTenantPackageEntity)) {
			throw ServiceExceptionUtil.exception(ErrorCodeEnum.TENANT_PACKAGE_NOT_EXISTS);
		}
		if (ObjUtil.equals(CommonConstants.STATUS_DEL, sysTenantPackageEntity.getStatus())) {
			throw ServiceExceptionUtil.exception(ErrorCodeEnum.TENANT_PACKAGE_DISABLE, sysTenantPackageEntity.getName());
		}
		return sysTenantPackageEntity;
	}
}
