package com.example.httpclientdemo.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RedisController {

    private  RedisProperties redisProperties;

    @Autowired
    public RedisController(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @GetMapping("/redisInfo")
    @HystrixCommand
    public RedisProperties getRedisProperties() {
        return redisProperties;
    }

}
