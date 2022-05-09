package com.guavapay.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.SerializationCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class RedisConfiguration {

    @Value(value = "${redis.url}")
    private String url;

    @Profile({"local"})
    @Bean
    RedissonClient redissonOperationSingleClient() {
        Config config = new Config();
        config.setCodec(new SerializationCodec());

        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(url);
        return Redisson.create(config);
    }

}
