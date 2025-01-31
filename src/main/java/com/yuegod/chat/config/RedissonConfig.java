package com.yuegod.chat.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author YueGod
 * @since 2025/2/1
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedissonConfig {

  private final RedisProperties redisProperties;

  public RedissonConfig(RedisProperties redisProperties) {
    this.redisProperties = redisProperties;
  }

  @Bean
  public RedissonClient redissonClient() {
    Config config = new Config();
    config
        .useSingleServer()
        .setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
        .setConnectionPoolSize(500)
        .setSubscriptionConnectionPoolSize(redisProperties.getJedis().getPool().getMaxActive())
        .setIdleConnectionTimeout(30000)
        .setConnectionMinimumIdleSize(300);

    return Redisson.create(config);
  }
}
