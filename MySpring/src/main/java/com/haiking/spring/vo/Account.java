package com.haiking.spring.vo;

public class Account {
	private String userName;
	private String cardNo;
	private double money;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	@Override
	public String toString() {
		return "Account [userName=" + userName + ", cardNo=" + cardNo + ", money=" + money + "]";
	}
}
