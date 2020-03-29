package com.haiking.spring.utils;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.sql.SQLException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;

import com.haiking.spring.annotation.MyAutowired;
import com.haiking.spring.annotation.MyService;
import com.haiking.spring.annotation.MyTransational;

@MyService
public class AopExtTransaction {
    // 一个事务实例子 针对一个事务
    @MyAutowired
    private TransactionManager transactionManager;

    // 使用异常通知进行 回滚事务
    @AfterThrowing("execution(* com.haiking.spring.*..*(..))")
    public void afterThrowing() throws SQLException {
        // 获取当前事务进行回滚
        // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        transactionManager.rollback();
    }

    // 环绕通知 在方法之前和之后处理事情
    @Around("execution(* com.haiking.spring.*..*(..))")
    public void around(ProceedingJoinPoint pjp) throws Throwable {
        // 1.获取该方法上是否加上注解
        MyTransational extTransaction = getMethodExtTransaction(pjp);
        begin(extTransaction);
        // 2.调用目标代理对象方法
        pjp.proceed();
        // 3.判断该方法上是否就上注解
        commit();
    }

    private void begin(MyTransational extTransaction) throws SQLException {
        if (extTransaction == null) {
            return;
        }
        // 2.如果存在事务注解,开启事务
        transactionManager.beginTransaction();
    }

    private void commit() throws SQLException {
        // 5.如果存在注解,提交事务
        transactionManager.commit();
    }

    // 获取方法上是否存在事务注解
    private MyTransational getMethodExtTransaction(ProceedingJoinPoint pjp)
            throws NoSuchMethodException, SecurityException {
        String methodName = pjp.getSignature().getName();
        // 获取目标对象
        Class<?> classTarget = pjp.getTarget().getClass();
        // 获取目标对象类型
        Class<?>[] par = ((Executable) pjp.getSignature()).getParameterTypes();
        // 获取目标对象方法
        Method objMethod = classTarget.getMethod(methodName, par);
        MyTransational extTransaction = objMethod.getDeclaredAnnotation(MyTransational.class);
        return extTransaction;
    }

}
