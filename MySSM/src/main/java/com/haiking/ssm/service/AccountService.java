package com.haiking.ssm.service;

import com.haiking.ssm.pojo.Account;

import java.util.List;

public interface AccountService {
    List<Account> queryAccountList() throws Exception;
}
