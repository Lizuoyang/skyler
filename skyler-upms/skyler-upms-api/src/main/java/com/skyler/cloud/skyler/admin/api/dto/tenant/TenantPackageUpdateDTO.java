package com.skyler.cloud.skyler.admin.api.dto.tenant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 租户套餐修改传输对象
 * </p>
 *
 * @author lzy
 * @since 2023/10/27 9:52
 */
@Data
@Schema(description = "租户套餐修改传输对象")
@EqualsAndHashCode(callSuper = true)
public class TenantPackageUpdateDTO extends TenantPackageBaseDTO {
	/**
	 * 套餐编号
	 */
	@Schema(description="套餐编号")
	@NotNull(message = "套餐编号不能为空")
	private Long id;
}
