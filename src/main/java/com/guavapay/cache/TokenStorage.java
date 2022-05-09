package com.guavapay.cache;

import com.guavapay.constant.Constants;
import com.guavapay.model.dto.TokenPair;
import com.guavapay.security.Principal;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenStorage {

    @Value("${application.security.authentication.jwt.token-validity-in-seconds}")
    private long tokenValidityInSeconds;

    private final RedissonClient redissonClient;

    public void save(TokenPair tokenPair, Long accountId) {
        RBucket<TokenPair> bucket = redissonClient.getBucket(buildBucketName(accountId));
        bucket.set(tokenPair, new Date((new Date()).getTime() + tokenValidityInSeconds * 1000).getTime(), TimeUnit.SECONDS);
    }

    public void delete(Long accountId) {
        RBucket<TokenPair> bucket = redissonClient.getBucket(buildBucketName(accountId));
        if (Objects.nonNull(bucket)) bucket.delete();
    }

    public TokenPair read(@NonNull Principal principal) {
        RBucket<TokenPair> bucket = redissonClient.getBucket(buildBucketName(principal.getId()));
        return bucket.get();
    }

    private String buildBucketName(@NonNull Long accountId) {
        return String.format("%s:%s", Constants.TOKEN_CACHE_PREFIX, accountId);
    }
}
