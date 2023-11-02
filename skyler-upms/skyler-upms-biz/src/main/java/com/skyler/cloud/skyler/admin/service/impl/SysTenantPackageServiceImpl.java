package com.skyler.cloud.skyler.admin.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skyler.cloud.skyler.admin.api.convert.tenant.TenantPackageConvert;
import com.skyler.cloud.skyler.admin.api.dto.tenant.TenantPackageCreateDTO;
import com.skyler.cloud.skyler.admin.api.dto.tenant.TenantPackageUpdateDTO;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantEntity;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantPackageEntity;
import com.skyler.cloud.skyler.admin.mapper.SysTenantPackageMapper;
import com.skyler.cloud.skyler.admin.service.SysTenantPackageService;
import com.skyler.cloud.skyler.admin.service.SysTenantService;
import com.skyler.cloud.skyler.common.core.constant.CommonConstants;
import com.skyler.cloud.skyler.common.core.exception.enums.ErrorCodeEnum;
import com.skyler.cloud.skyler.common.core.exception.utils.ServiceExceptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 租户套餐表
 *
 * @author skyler
 * @date 2023-10-26 14:09:01
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SysTenantPackageServiceImpl extends ServiceImpl<SysTenantPackageMapper, SysTenantPackageEntity> implements SysTenantPackageService {

	private SysTenantService tenantService;

	@Autowired
	public void setSysTenantService(@Lazy SysTenantService tenantService) {
		this.tenantService = tenantService;
	}

	@Override
	public boolean saveTenantPackage(TenantPackageCreateDTO tenantPackageDTO) {
		SysTenantPackageEntity saveEntity = TenantPackageConvert.INSTANCE.convert(tenantPackageDTO);
		return this.save(saveEntity);
	}

	@Override
	public void updateTeantPackage(TenantPackageUpdateDTO updateDTO) {
		// 校验存在
		SysTenantPackageEntity tenantPackage = validTenantPackage(updateDTO.getId());
		// 更新套餐
		SysTenantPackageEntity updateEntity = TenantPackageConvert.INSTANCE.convert(updateDTO);
		this.updateById(updateEntity);

		// 如果菜单发生变化，更新租户的菜单
		if (!StrUtil.equals(tenantPackage.getMenuIds(), updateDTO.getMenuIds())) {
			List<SysTenantEntity> tenantEntities = tenantService.list(Wrappers.<SysTenantEntity>lambdaQuery().eq(SysTenantEntity::getPackageId, tenantPackage.getId()));
			tenantEntities.forEach(t -> tenantService.updateTenantRoleMenu(t.getId(), updateDTO.getMenuIds()));
		}
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
