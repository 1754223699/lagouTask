package com.haiking.ssm.service.impl;

import com.haiking.ssm.dao.AccountMapper;
import com.haiking.ssm.pojo.Account;
import com.haiking.ssm.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public List<Account> queryAccountList() throws Exception {
        return accountMapper.queryAccountList();
    }
}
