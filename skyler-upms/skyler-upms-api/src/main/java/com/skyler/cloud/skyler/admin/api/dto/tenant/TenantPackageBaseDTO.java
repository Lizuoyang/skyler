package com.skyler.cloud.skyler.admin.api.dto.tenant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 套餐基础传输对象
 * </p>
 *
 * @author lzy
 * @since 2023/11/1 14:54
 */
@Data
public class TenantPackageBaseDTO {
	/**
	 * 套餐名
	 */
	@Schema(description="套餐名")
	@NotNull(message = "套餐名不能为空")
	private String name;

	/**
	 * 备注
	 */
	@Schema(description="备注")
	private String remark;

	/**
	 * 关联的菜单编号
	 */
	@Schema(description="关联的菜单编号")
	@NotNull(message = "关联的菜单编号不能为空")
	private String menuIds;
}
