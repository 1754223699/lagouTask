package com.haiking.spring.service;

public interface TransferService {
    void transfer(String fromCardNo, String toCardNo, Double money) throws Exception;
}
