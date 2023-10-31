package com.skyler.cloud.skyler.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import com.skyler.cloud.skyler.admin.api.entity.SysTenantEntity;
import com.skyler.cloud.skyler.admin.service.SysTenantService;
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
 * 租户表
 *
 * @author skyler
 * @date 2023-10-24 11:44:11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tenant" )
@Tag(description = "tenant" , name = "租户表管理" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysTenantController {

    private final  SysTenantService sysTenantService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param sysTenant 租户表
     * @return
     */
    @Operation(summary = "分页查询" , description = "分页查询" )
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('admin_tenant_view')" )
    public R getSysTenantPage(@ParameterObject Page page, @ParameterObject SysTenantEntity sysTenant) {
        LambdaQueryWrapper<SysTenantEntity> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StrUtil.isNotBlank(sysTenant.getName()),SysTenantEntity::getName,sysTenant.getName());
		wrapper.eq(Objects.nonNull(sysTenant.getStatus()),SysTenantEntity::getStatus,sysTenant.getStatus());
        return R.ok(sysTenantService.page(page, wrapper));
    }


    /**
     * 通过id查询租户表
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id查询" , description = "通过id查询" )
    @GetMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('admin_tenant_view')" )
    public R getById(@PathVariable("id" ) Long id) {
        return R.ok(sysTenantService.getById(id));
    }

    /**
     * 新增租户表
     * @param sysTenant 租户表
     * @return R
     */
    @Operation(summary = "新增租户表" , description = "新增租户表" )
    @SysLog("新增租户表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin_tenant_add')" )
    public R save(@RequestBody SysTenantEntity sysTenant) {
        return R.ok(sysTenantService.save(sysTenant));
    }

    /**
     * 修改租户表
     * @param sysTenant 租户表
     * @return R
     */
    @Operation(summary = "修改租户表" , description = "修改租户表" )
    @SysLog("修改租户表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin_tenant_edit')" )
    public R updateById(@RequestBody SysTenantEntity sysTenant) {
        return R.ok(sysTenantService.updateById(sysTenant));
    }

    /**
     * 通过id删除租户表
     * @param ids id列表
     * @return R
     */
    @Operation(summary = "通过id删除租户表" , description = "通过id删除租户表" )
    @SysLog("通过id删除租户表" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('admin_tenant_del')" )
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(sysTenantService.removeBatchByIds(CollUtil.toList(ids)));
    }


    /**
     * 导出excel 表格
     * @param sysTenant 查询条件
   	 * @param ids 导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @PreAuthorize("@pms.hasPermission('admin_tenant_export')" )
    public List<SysTenantEntity> export(SysTenantEntity sysTenant,Long[] ids) {
        return sysTenantService.list(Wrappers.lambdaQuery(sysTenant).in(ArrayUtil.isNotEmpty(ids), SysTenantEntity::getId, ids));
    }
}
