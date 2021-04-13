package com.tommy.spring.config;

/**
 * @Author Tommy
 * 2021/4/8
 */

import org.hibernate.dialect.MySQL5Dialect;

/**
 * 配置一个jpa生成字段为中文的类
 */
public class JpaFormatConfig extends MySQL5Dialect {
    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}
