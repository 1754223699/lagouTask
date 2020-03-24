package com.haiking.ssm.controller;

import com.haiking.ssm.pojo.Account;
import com.haiking.ssm.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping("/queryAll")
    @ResponseBody
    public List<Account> queryAll() throws Exception {
        List<Account> accounts = accountService.queryAccountList();
        return accounts;
    }

}
