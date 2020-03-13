package com.haiking.spring.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.haiking.spring.annotation.MyAutowired;
import com.haiking.spring.annotation.MyService;
import com.haiking.spring.dao.TransferDao;
import com.haiking.spring.vo.Account;

@MyService
public class TransferDaoImpl implements TransferDao {
	@MyAutowired
    private JdbcTemplate jdbcTemplate;
	
	public int transferMoneyByCardNo(Account account) {
		String sql = "update account set money=? where cardNo=?";
        return jdbcTemplate.update(sql,account.getMoney(),account.getCardNo());
	}

	public Account queryAccountByCardNo(String cardNo) throws Exception {
		String sql = "select * from account where cardNo=?";
        return jdbcTemplate.queryForObject(sql, new RowMapper<Account>() {
            public Account mapRow(ResultSet resultSet, int i) throws SQLException {
                Account account = new Account();
                account.setUserName(resultSet.getString("username"));
                account.setCardNo(resultSet.getString("cardNo"));
                account.setMoney(resultSet.getInt("money"));
                return account;
            }
        }, cardNo);
	}
}
