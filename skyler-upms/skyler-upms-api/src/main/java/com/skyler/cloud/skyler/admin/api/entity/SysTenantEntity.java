package com.skyler.cloud.skyler.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.skyler.cloud.skyler.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 租户表
 *
 * @author skyler
 * @date 2023-10-24 11:44:11
 */
@Data
@TableName("sys_tenant")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "租户表")
public class SysTenantEntity extends BaseEntity {

	/**
	 * 套餐编号 - 系统
	 */
	public static final Long PACKAGE_ID_SYSTEM = 0L;

	/**
	* 租户编号
	*/
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="租户编号")
    private Long id;

	/**
	* 租户名
	*/
    @Schema(description="租户名")
    private String name;

	/**
	* 联系人的用户编号
	*/
    @Schema(description="联系人的用户编号")
    private Long contactUserId;

	/**
	* 联系人
	*/
    @Schema(description="联系人")
    private String contactName;

	/**
	* 联系手机
	*/
    @Schema(description="联系手机")
    private String contactMobile;

	/**
	* 租户状态
	*/
    @Schema(description="租户状态")
    private Integer status;

	/**
	* 绑定域名
	*/
    @Schema(description="绑定域名")
    private String domain;

	/**
	* 租户套餐编号
	*/
    @Schema(description="租户套餐编号")
    private Long packageId;

	/**
	* 过期时间
	*/
    @Schema(description="过期时间")
    private LocalDateTime expireTime;

	/**
	* 账号数量
	*/
    @Schema(description="账号数量")
    private Integer accountCount;

}
