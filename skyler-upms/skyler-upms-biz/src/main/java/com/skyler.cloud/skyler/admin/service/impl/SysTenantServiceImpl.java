package com.skyler.cloud.skyler.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantEntity;
import com.skyler.cloud.skyler.admin.mapper.SysTenantMapper;
import com.skyler.cloud.skyler.admin.service.SysTenantService;
import org.springframework.stereotype.Service;
/**
 * 租户表
 *
 * @author skyler
 * @date 2023-10-24 11:44:11
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenantEntity> implements SysTenantService {
}
