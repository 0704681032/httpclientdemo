package com.example.httpclientdemo.controller;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.JedisClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

//JedisConnectionConfiguration
//但为啥如下关键参数未生效呢? 而是使用了JedisClient的默认值(即60s)呢?
//spring.redis.jedis.pool.min-evictable-idle-time-millis=-1 // 不要因为idleTime大于某个阈值从而把连接给删除掉. 这样可以防止无意义的大规模连接重建.
@Component
@ConditionalOnClass({ GenericObjectPool.class, JedisConnection.class, Jedis.class })
public class MyJedisClientConfigurationBuilderCustomizer implements JedisClientConfigurationBuilderCustomizer {

    @Value("${spring.redis.jedis.pool.min-evictable-idle-time-millis:60000}")
    private long minEvictableIdleTimeMillis;

    private  RedisProperties redisProperties;

    @Autowired
    public MyJedisClientConfigurationBuilderCustomizer(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }


    @Override
    public void customize(JedisClientConfiguration.JedisClientConfigurationBuilder clientConfigurationBuilder) {
        clientConfigurationBuilder.usePooling().poolConfig(jedisPoolConfig(redisProperties.getJedis().getPool()));//重新设置jdis的相关参数
    }

    private JedisPoolConfig jedisPoolConfig(RedisProperties.Pool pool) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(pool.getMaxActive());
        config.setMaxIdle(pool.getMaxIdle());
        config.setMinIdle(pool.getMinIdle());
        if (pool.getTimeBetweenEvictionRuns() != null) {
            config.setTimeBetweenEvictionRuns(pool.getTimeBetweenEvictionRuns());
        }
        if (pool.getMaxWait() != null) {
            config.setMaxWait(pool.getMaxWait());
        }
        config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);//新增参数
        return config;
    }
}
