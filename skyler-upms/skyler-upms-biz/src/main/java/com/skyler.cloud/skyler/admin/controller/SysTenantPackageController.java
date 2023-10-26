package com.skyler.cloud.skyler.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantPackageEntity;
import com.skyler.cloud.skyler.admin.service.SysTenantPackageService;
import com.skyler.cloud.skyler.common.core.util.R;
import com.skyler.cloud.skyler.common.log.annotation.SysLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * 租户套餐表
 *
 * @author skyler
 * @date 2023-10-26 14:12:23
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tenantPackge" )
@Tag(description = "tenantPackge" , name = "租户套餐表管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysTenantPackageController {

    private final  SysTenantPackageService sysTenantPackageService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param sysTenantPackage 租户套餐表
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('admin_tenantPackge_view')" )
    public R getSysTenantPackagePage(@ParameterObject Page page, @ParameterObject SysTenantPackageEntity sysTenantPackage) {
        LambdaQueryWrapper<SysTenantPackageEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StrUtil.isNotBlank(sysTenantPackage.getName()),SysTenantPackageEntity::getName,sysTenantPackage.getName());
		wrapper.eq(Objects.nonNull(sysTenantPackage.getStatus()),SysTenantPackageEntity::getStatus,sysTenantPackage.getStatus());
        return R.ok(sysTenantPackageService.page(page, wrapper));
    }


    /**
     * 通过id查询租户套餐表
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('admin_tenantPackge_view')" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(sysTenantPackageService.getById(id));
    }

    /**
     * 新增租户套餐表
     * @param sysTenantPackage 租户套餐表
     * @return R
     */
    @Operation(summary = "新增租户套餐表" , description = "新增租户套餐表" )
    @SysLog("新增租户套餐表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin_tenantPackge_add')" )
    public R save(@RequestBody SysTenantPackageEntity sysTenantPackage) {
        return R.ok(sysTenantPackageService.save(sysTenantPackage));
    }

    /**
     * 修改租户套餐表
     * @param sysTenantPackage 租户套餐表
     * @return R
     */
    @Operation(summary = "修改租户套餐表" , description = "修改租户套餐表" )
    @SysLog("修改租户套餐表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin_tenantPackge_edit')" )
    public R updateById(@RequestBody SysTenantPackageEntity sysTenantPackage) {
        return R.ok(sysTenantPackageService.updateById(sysTenantPackage));
    }

    /**
     * 通过id删除租户套餐表
     * @param ids id列表
     * @return R
     */
    @Operation(summary = "通过id删除租户套餐表" , description = "通过id删除租户套餐表" )
    @SysLog("通过id删除租户套餐表" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('admin_tenantPackge_del')" )
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(sysTenantPackageService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param sysTenantPackage 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('admin_tenantPackge_export')" )
    public List<SysTenantPackageEntity> export(SysTenantPackageEntity sysTenantPackage,Long[] ids) {
        return sysTenantPackageService.list(Wrappers.lambdaQuery(sysTenantPackage).in(ArrayUtil.isNotEmpty(ids), SysTenantPackageEntity::getId, ids));
    }
}
