package com.springcloud.configClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * @auther: 坎布里奇
 * @description: xxxxx
 * @date: 2021/2/14 14:43
 * @version: 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class SpringCloudConfigClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigClientApplication.class, args);
    }

}
