package com.skyler.cloud.skyler.common.mybatis.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 租户实体
 * </p>
 *
 * @author lzy
 * @since 2023/11/2 11:07
 */
@Getter
@Setter
public class TenantEntity extends BaseEntity implements Serializable {
	/**
	 * 多租户编号
	 */
	@Schema(description = "租户编号")
	private Long tenantId;
}
