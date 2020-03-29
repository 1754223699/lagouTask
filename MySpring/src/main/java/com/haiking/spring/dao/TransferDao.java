package com.haiking.spring.dao;

import com.haiking.spring.vo.Account;

public interface TransferDao {

    public Account queryAccountByCardNo(String cardNo) throws Exception;

    public int transferMoneyByCardNo(Account account) throws Exception;
}
