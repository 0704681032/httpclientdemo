package com.example.httpclientdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

//JedisConnectionConfiguration
//本机没有安装redis,但是又要学习data redis的源代码
@SpringBootApplication(exclude = RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisProperties.class)
public class HttpclientdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpclientdemoApplication.class, args);
	}

}
