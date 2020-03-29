package com.haiking.ssm.dao;

import com.haiking.ssm.pojo.Account;

import java.util.List;

public interface AccountMapper {
    List<Account> queryAccountList() throws Exception;
}
