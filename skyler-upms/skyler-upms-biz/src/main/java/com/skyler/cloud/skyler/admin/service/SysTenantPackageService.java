package com.skyler.cloud.skyler.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.skyler.cloud.skyler.admin.api.dto.TenantPackageDTO;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantPackageEntity;

public interface SysTenantPackageService extends IService<SysTenantPackageEntity> {

    boolean saveTenantMenu(TenantPackageDTO tenantPackageDTO);
}
