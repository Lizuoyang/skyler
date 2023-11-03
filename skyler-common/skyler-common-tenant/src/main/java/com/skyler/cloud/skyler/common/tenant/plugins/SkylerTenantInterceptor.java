package com.skyler.cloud.skyler.common.tenant.plugins;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.skyler.cloud.skyler.common.tenant.context.TenantContextHolder;
import com.skyler.cloud.skyler.common.tenant.props.TenantProperties;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

/**
 * <p>
 * 多租户拦截器
 * </p>
 *
 * @author lzy
 * @since 2023/11/3 9:55
 */
public class SkylerTenantInterceptor implements TenantLineHandler {
	private TenantProperties tenantProperties;

	public SkylerTenantInterceptor(TenantProperties tenantProperties) {
		this.tenantProperties = tenantProperties;
	}

	@Override
	public Expression getTenantId() {
		return new LongValue(TenantContextHolder.getRequiredTenantId());
	}

	@Override
	public String getTenantIdColumn() {
		return tenantProperties.getColumn();
	}

	@Override
	public boolean ignoreTable(String tableName) {
		return tenantProperties.getExclusionTable().stream().anyMatch(
				(t) -> t.equalsIgnoreCase(tableName)
		);
	}
}
