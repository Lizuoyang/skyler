package com.skyler.cloud.skyler.daemon.quartz;

import com.skyler.cloud.skyler.common.feign.annotation.EnableSkylerFeignClients;
import com.skyler.cloud.skyler.common.security.annotation.EnableSkylerResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author frwcloud
 * @date 2023-07-05
 */
@EnableSkylerFeignClients
@EnableSkylerResourceServer
@EnableDiscoveryClient
@SpringBootApplication
public class SkylerQuartzApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkylerQuartzApplication.class, args);
	}

}
