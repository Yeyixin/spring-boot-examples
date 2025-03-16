package com.neo;

import com.neo.model.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() {
        stringRedisTemplate.opsForValue().set("aaa", "111");
        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
    }

    @Test
    public void testObj() {
        try {
            // 创建 User 对象
            User user = new User("aa@126.com", "aa", "aa123456", "aa", "123");

            // 设置 Redis 键值对
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set("com.neox", user);
            operations.set("com.neo.f", user, 1, TimeUnit.SECONDS);

            // 等待键过期
            Thread.sleep(1000);

            // 验证键是否存在
            boolean exists = Boolean.TRUE.equals(redisTemplate.hasKey("com.neo.f"));
            if (exists) {
                System.out.println("exists is true");
            } else {
                System.out.println("exists is false");
            }

            // 断言键已过期
            Assert.assertNotEquals(Boolean.TRUE, redisTemplate.hasKey("com.neo.f"));

        } catch (InterruptedException e) {
            // 捕获线程中断异常并记录日志
            System.err.println("Thread was interrupted: " + e.getMessage());
            // 恢复线程中断状态
            Thread.currentThread().interrupt();
        }
    }

    @After
    public void cleanup() {
        // 清理 Redis 中的临时键值对
        stringRedisTemplate.delete("aaa");
        redisTemplate.delete("com.neox");
        redisTemplate.delete("com.neo.f");
    }
}
