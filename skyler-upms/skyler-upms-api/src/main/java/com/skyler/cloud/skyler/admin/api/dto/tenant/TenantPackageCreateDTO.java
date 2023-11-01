package com.skyler.cloud.skyler.admin.api.dto.tenant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 租户套餐新增 传输对象
 * </p>
 *
 * @author lzy
 * @since 2023/10/27 9:52
 */
@Data
@Schema(description = "租户套餐新增传输对象")
@EqualsAndHashCode(callSuper = true)
public class TenantPackageCreateDTO extends TenantPackageBaseDTO {

}
