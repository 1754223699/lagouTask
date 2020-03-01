package com.lagou.dao;

import java.util.List;

import com.lagou.vo.User;

public interface IUserDao {
		//查询所有
		public List<User> selectAll() throws Exception;
		//根据条件查询单个
		public User selectOne(User user) throws Exception;
		//删除单个
		public Integer deleteOne(User user) throws Exception;
		//添加单个
		public Integer insertOne(User user) throws Exception;
		//更新单个
		public Integer updateOne(User user) throws Exception;
}
