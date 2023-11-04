package com.example.httpclientdemo;

import com.netflix.config.ConfigurationManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.IOException;

//JedisConnectionConfiguration
//本机没有安装redis,但是又要学习data redis的源代码
@SpringBootApplication(exclude = RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisProperties.class)
//@EnableHystrix
//@EnableHystrixDashboard
public class HttpclientdemoApplication {

	public static void main(String[] args) {

		try {
			ConfigurationManager.loadCascadedPropertiesFromResources("application");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}


		SpringApplication.run(HttpclientdemoApplication.class, args);
	}

}
