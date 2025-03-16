package com.neo.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                // 确保线程安全，并处理 null 参数
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName()).append(".").append(method.getName());
                if (null != params && params.length > 0) {
                    for (Object obj : params) {
                        sb.append("_").append(obj == null ? "null" : obj.toString());
                    }
                }
                return sb.toString();
            }
        };
    }
}
