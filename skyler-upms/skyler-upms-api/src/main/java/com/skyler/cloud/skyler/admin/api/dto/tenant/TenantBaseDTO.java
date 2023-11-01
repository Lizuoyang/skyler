package com.skyler.cloud.skyler.admin.api.dto.tenant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>
 * 租户基础传输对象
 * </p>
 *
 * @author lzy
 * @since 2023/11/1 14:48
 */
@Data
public class TenantBaseDTO {
	/**
	 * 租户名
	 */
	@Schema(description="租户名")
	@NotNull(message = "租户名不能为空")
	private String name;

	/**
	 * 联系人
	 */
	@Schema(description="联系人")
	@NotNull(message = "联系人不能为空")
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
	@NotNull(message = "租户套餐编号不能为空")
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
