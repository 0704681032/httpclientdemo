package com.example.httpclientdemo.hystrix;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HystrixConfig implements InitializingBean {

    @Bean
    public HystrixCommandAspect hystrixCommandAspect(){
        return new HystrixCommandAspect();  // 初始化切面
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化熔断器配置
        // 清除配置
        ConfigurationManager.getConfigInstance().clear();
        ConfigurationManager.loadCascadedPropertiesFromResources("application");
    }
}
