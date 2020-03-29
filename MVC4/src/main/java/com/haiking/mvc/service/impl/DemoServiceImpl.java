package com.haiking.mvc.service.impl;

import com.haiking.mvc.annotation.KingService;
import com.haiking.mvc.service.IDemoService;

@KingService("demoService")
public class DemoServiceImpl implements IDemoService {
    @Override
    public String get(String name) {
        System.out.println("service 实现类中的name参数：" + name);
        return name;
    }
}
