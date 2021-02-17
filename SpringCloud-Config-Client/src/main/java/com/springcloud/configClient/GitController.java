package com.springcloud.configClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther: 坎布里奇
 * @description: xxxxx
 * @date: 2021/2/14 17:18
 * @version: 1.0.0
 */
@RestController
public class GitController {

    @Autowired
    private ConfigServerConfig configServerConfig;

    @RequestMapping(value = "/hi")
    public Object hi(){
        return configServerConfig.getFoo();
    }
}
