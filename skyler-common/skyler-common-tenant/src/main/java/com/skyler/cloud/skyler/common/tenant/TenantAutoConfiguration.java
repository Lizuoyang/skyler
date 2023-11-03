package com.skyler.cloud.skyler.common.tenant;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.skyler.cloud.skyler.common.core.constant.enums.WebFilterOrderEnum;
import com.skyler.cloud.skyler.common.mybatis.utils.MyBatisUtils;
import com.skyler.cloud.skyler.common.tenant.plugins.SkylerTenantInterceptor;
import com.skyler.cloud.skyler.common.tenant.props.TenantProperties;
import com.skyler.cloud.skyler.common.tenant.web.TenantContextWebFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 多租户统一配置
 * </p>
 *
 * @author lzy
 * @since 2023/11/1 9:19
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "tenant", value = "enable", matchIfMissing = true) // 允许使用 tenant.enable=false 禁用多租户
@EnableConfigurationProperties(TenantProperties.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TenantAutoConfiguration {
	private final TenantProperties tenantProperties;

	@Bean
	public TenantLineInnerInterceptor tenantLineInnerInterceptor(MybatisPlusInterceptor interceptor) {
		TenantLineInnerInterceptor tenantLineInnerInterceptor = new TenantLineInnerInterceptor(new SkylerTenantInterceptor(tenantProperties));
		MyBatisUtils.addInterceptor(interceptor,tenantLineInnerInterceptor, 0);
		return tenantLineInnerInterceptor;
	}

	@Bean
	public FilterRegistrationBean<TenantContextWebFilter> tenantContextWebFilter() {
		FilterRegistrationBean<TenantContextWebFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new TenantContextWebFilter());
		registrationBean.setOrder(WebFilterOrderEnum.TENANT_CONTEXT_FILTER);
		return registrationBean;
	}


}
