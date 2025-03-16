package com.neo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Address {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 用户ID，不能为空。
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * 省份信息，不能为空，最大长度为 50。
     */
    @Column(nullable = false, length = 50)
    private String province;

    /**
     * 城市信息，不能为空，最大长度为 50。
     */
    @Column(nullable = false, length = 50)
    private String city;

    /**
     * 街道信息，不能为空，最大长度为 100。
     */
    @Column(nullable = false, length = 100)
    private String street;

    /**
     * 全参构造函数，用于初始化所有字段。
     */
    public Address(Long userId, String province, String city, String street) {
        this.userId = userId;
        this.province = province;
        this.city = city;
        this.street = street;
    }

    public void setUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        this.userId = userId;
    }

    /**
     * 设置省份信息。
     * @param province 省份名称，不能为空且长度不超过 50。
     */
    public void setProvince(String province) {
        if (province == null || province.trim().isEmpty()) {
            throw new IllegalArgumentException("Province cannot be null or empty");
        }
        if (province.length() > 50) {
            throw new IllegalArgumentException("Province length exceeds the maximum allowed (50 characters)");
        }
        this.province = province;
    }

    /**
     * 设置城市信息。
     * @param city 城市名称，不能为空且长度不超过 50。
     */
    public void setCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }
        if (city.length() > 50) {
            throw new IllegalArgumentException("City length exceeds the maximum allowed (50 characters)");
        }
        this.city = city;
    }

    /**
     * 设置街道信息。
     * @param street 街道名称，不能为空且长度不超过 100。
     */
    public void setStreet(String street) {
        if (street == null || street.trim().isEmpty()) {
            throw new IllegalArgumentException("Street cannot be null or empty");
        }
        if (street.length() > 100) {
            throw new IllegalArgumentException("Street length exceeds the maximum allowed (100 characters)");
        }
        this.street = street;
    }
}
