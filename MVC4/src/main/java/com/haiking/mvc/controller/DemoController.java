package com.haiking.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.haiking.mvc.annotation.KingAutowired;
import com.haiking.mvc.annotation.KingController;
import com.haiking.mvc.annotation.KingRequestMapping;
import com.haiking.mvc.annotation.Security;
import com.haiking.mvc.service.IDemoService;

@KingController
@KingRequestMapping("/demo")
public class DemoController {

    @KingAutowired
    private IDemoService demoService;

    @Security({"zhangsan", "lisi"})
    @KingRequestMapping("/query")
    public String query(HttpServletRequest request, HttpServletResponse response, String name) {
        return demoService.get(name);
    }
}
