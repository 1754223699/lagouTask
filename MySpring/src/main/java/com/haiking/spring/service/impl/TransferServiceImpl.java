package com.haiking.spring.service.impl;

import com.haiking.spring.annotation.MyAutowired;
import com.haiking.spring.annotation.MyService;
import com.haiking.spring.annotation.MyTransational;
import com.haiking.spring.dao.TransferDao;
import com.haiking.spring.service.TransferService;
import com.haiking.spring.vo.Account;

@MyService
public class TransferServiceImpl implements TransferService {

    @MyAutowired
    private TransferDao transferDao;

    @MyTransational
    public void transfer(String fromCardNo, String toCardNo, Double money) throws Exception {

        Account from = transferDao.queryAccountByCardNo(fromCardNo);
        Account to = transferDao.queryAccountByCardNo(toCardNo);

        from.setMoney(from.getMoney() - money);
        to.setMoney(to.getMoney() + money);

        transferDao.transferMoneyByCardNo(to);
        int c = 1 / 0;
        transferDao.transferMoneyByCardNo(from);


    }

}
