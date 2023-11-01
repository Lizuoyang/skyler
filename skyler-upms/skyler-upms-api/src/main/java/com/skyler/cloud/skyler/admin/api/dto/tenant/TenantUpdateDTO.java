package com.skyler.cloud.skyler.admin.api.dto.tenant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 租户传输对象
 * </p>
 *
 * @author lzy
 * @since 2023/10/31 15:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TenantUpdateDTO extends TenantBaseDTO {
	/**
	 * 租户编号
	 */
	@Schema(description="租户编号")
	@NotNull(message = "租户编号不能为空")
	private Long id;
}
