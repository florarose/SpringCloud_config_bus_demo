package com.springcloud.configClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @auther: 坎布里奇
 * @description: xxxxx
 * @date: 2021/2/14 17:17
 * @version: 1.0.0
 */
@Component
public class ConfigServerConfig {


    // 获取的是, 配置中心，配置文件中的属性。
    @Value("${foo}")
    String foo;


    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }
}
