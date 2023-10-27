package com.skyler.cloud.skyler.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skyler.cloud.skyler.admin.api.dto.TenantPackageDTO;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantPackageEntity;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantPackageMenuEntity;
import com.skyler.cloud.skyler.admin.mapper.SysTenantPackageMapper;
import com.skyler.cloud.skyler.admin.service.SysTenantPackageMenuService;
import com.skyler.cloud.skyler.admin.service.SysTenantPackageService;
import com.skyler.cloud.skyler.common.core.constant.CacheConstants;
import com.skyler.cloud.skyler.common.core.util.BeanCopyUtils;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 租户套餐表
 *
 * @author skyler
 * @date 2023-10-26 14:09:01
 */
@Service
@AllArgsConstructor
public class SysTenantPackageServiceImpl extends ServiceImpl<SysTenantPackageMapper, SysTenantPackageEntity> implements SysTenantPackageService {

	private final CacheManager cacheManager;
	private final SysTenantPackageMenuService tenantPackageMenuService;

	@Override
	public boolean saveTenantMenu(TenantPackageDTO tenantPackageDTO) {
		SysTenantPackageEntity saveEntity = new SysTenantPackageEntity();
		BeanCopyUtils.copy(tenantPackageDTO, saveEntity);
		this.save(saveEntity);

		if (StrUtil.isNotBlank(tenantPackageDTO.getMenuIds())) {
			List<SysTenantPackageMenuEntity> tenantMenuList = Arrays.stream(tenantPackageDTO.getMenuIds().split(StrUtil.COMMA)).map(menuId -> {
				SysTenantPackageMenuEntity tenantMenu = new SysTenantPackageMenuEntity();
				tenantMenu.setTenantPackageId(saveEntity.getId());
				tenantMenu.setMenuId(Long.valueOf(menuId));
				return tenantMenu;
			}).collect(Collectors.toList());

			// 清空userinfo
			cacheManager.getCache(CacheConstants.USER_DETAILS).clear();
			tenantPackageMenuService.saveBatch(tenantMenuList);
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
