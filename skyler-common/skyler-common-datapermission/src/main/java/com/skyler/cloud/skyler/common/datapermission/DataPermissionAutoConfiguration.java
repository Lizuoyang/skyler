package com.skyler.cloud.skyler.common.datapermission;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.skyler.cloud.skyler.common.datapermission.plugins.SkylerDataPermissionInterceptor;
import com.skyler.cloud.skyler.common.mybatis.utils.MyBatisUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 数据权限统一配置
 * </p>
 *
 * @author lzy
 * @since 2023/11/1 9:19
 */
@Configuration(proxyBeanMethods = false)
public class DataPermissionAutoConfiguration {
	/**
	 * 数据权限拦截器
	 */
	@Bean
	public SkylerDataPermissionInterceptor skylerDataPermissionInterceptor(MybatisPlusInterceptor interceptor) {
		SkylerDataPermissionInterceptor dataPermissionInterceptor = new SkylerDataPermissionInterceptor();
		MyBatisUtils.addInterceptor(interceptor, dataPermissionInterceptor,0);
		return dataPermissionInterceptor;
	}
}
