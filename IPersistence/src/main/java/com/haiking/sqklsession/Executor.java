package com.haiking.sqklsession;

import java.util.List;

import com.haiking.vo.Configuration;
import com.haiking.vo.MapperStatement;

public interface Executor {
	<E> List<E> query(Configuration configuration,MapperStatement mapperStatement ,Object... params) throws Exception;
	
	Integer delete(Configuration configuration,MapperStatement mapperStatement ,Object... params) throws Exception;
	
	Integer insert(Configuration configuration,MapperStatement mapperStatement ,Object... params) throws Exception;
	
	Integer update(Configuration configuration,MapperStatement mapperStatement ,Object... params) throws Exception;
}
