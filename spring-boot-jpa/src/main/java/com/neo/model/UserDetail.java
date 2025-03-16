package com.neo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@Entity
@SuperBuilder // 使用Lombok的SuperBuilder简化构造函数
public class UserDetail {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId; // 修改为String类型，避免length属性误用

    private Integer age;

    @Column(nullable = false)
    private String realName;

    private Status status; // 使用枚举类型替代String

    private String hobby;

    private String introduction;

    private String lastLoginIp;

    // 构造函数初始化字段
    public UserDetail(String userId, Integer age, String realName, Status status, String hobby, String introduction, String lastLoginIp) {
        validateAge(age); // 校验age
        validateRealName(realName); // 校验realName
        this.userId = Objects.requireNonNull(userId, "userId cannot be null");
        this.age = age;
        this.realName = realName;
        this.status = Objects.requireNonNull(status, "status cannot be null");
        this.hobby = hobby;
        this.introduction = introduction;
        this.lastLoginIp = lastLoginIp;
    }

    public UserDetail() {
        
    }

    // 校验age是否合法
    private void validateAge(Integer age) {
        if (age != null && age < 0) {
            throw new IllegalArgumentException("Age cannot be negative");
        }
    }

    // 校验realName是否合法
    private void validateRealName(String realName) {
        if (realName == null || realName.trim().isEmpty()) {
            throw new IllegalArgumentException("Real name cannot be empty or null");
        }
    }

    // Getter for status (返回String类型以保持兼容性)
    public String getStatus() {
        return status != null ? status.name() : null;
    }

    // Setter for status (支持从String转换为枚举类型)
    public void setStatus(String status) {
        try {
            this.status = Status.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }
    }

    @Override
    public String toString() {
        return "UserDetail{" +
                "id=" + id +
                ", userId='" + maskSensitiveData(userId) + '\'' + // 脱敏处理
                ", age=" + age +
                ", realName='" + realName + '\'' +
                ", status='" + status + '\'' +
                ", hobby='" + hobby + '\'' +
                ", introduction='" + introduction + '\'' +
                ", lastLoginIp='" + maskSensitiveData(lastLoginIp) + '\'' + // 脱敏处理
                '}';
    }

    // 脱敏处理方法
    private String maskSensitiveData(String data) {
        if (data == null || data.isEmpty()) {
            return data;
        }
        return data.replaceAll("(?<=\\w{3})\\w(?=\\w{3})", "*"); // 保留前后3个字符，中间用*代替
    }

    // 定义Status枚举类型
    public enum Status {
        ACTIVE, INACTIVE, SUSPENDED
    }
}
