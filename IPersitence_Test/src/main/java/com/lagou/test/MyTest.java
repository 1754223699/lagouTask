package com.lagou.test;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.haiking.io.Resources;
import com.haiking.sqklsession.SqlSession;
import com.haiking.sqklsession.SqlSessionFactory;
import com.haiking.sqklsession.SqlSessionFactoryBuiler;
import com.lagou.vo.User;

public class MyTest {

    @Test
    public void test() throws Exception {
        InputStream inputStream = Resources.getResourcesAsStream("/sqlMapConfig.xml");

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuiler().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSqlSession();

        User user = new User();
        user.setId(1);
        user.setUserName("shangsan");
        ;
        User user2 = sqlSession.selectOne("User.selectOne", user);
        System.out.println(user2);
    }

    @Test
    public void test2() throws Exception {
        InputStream inputStream = Resources.getResourcesAsStream("/sqlMapConfig.xml");

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuiler().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSqlSession();
        User user = new User();
        List<User> users = sqlSession.selectAll("User.selectAll", user);
        for (User user3 : users) {
            System.out.println(user3);
        }
    }

    @Test
    public void test3() throws Exception {
        InputStream inputStream = Resources.getResourcesAsStream("/sqlMapConfig.xml");

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuiler().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSqlSession();
        User user = new User();
        user.setId(1);
        user.setUserName("shangsan");
        Integer updateRows = sqlSession.deleteOne("User.deleteOne", user);
        System.out.println(updateRows);
    }

    @Test
    public void test4() throws Exception {
        InputStream inputStream = Resources.getResourcesAsStream("/sqlMapConfig.xml");

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuiler().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSqlSession();
        User user = new User();
        user.setId(1);
        user.setUserName("shangsan");
        user.setRealName("Hello");
        user.setPassWord("88888");
        Integer updateRows = sqlSession.insertOne("User.insertOne", user);
        System.out.println(updateRows);
    }

    @Test
    public void test5() throws Exception {
        InputStream inputStream = Resources.getResourcesAsStream("/sqlMapConfig.xml");

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuiler().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSqlSession();
        User user = new User();
        user.setId(1);
        user.setUserName("zhangsan");
        Integer updateRows = sqlSession.updateOne("User.updateOne", user);
        System.out.println(updateRows);
    }
}
