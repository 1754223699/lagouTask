package com.haiking.sqklsession;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.haiking.utils.GenericTokenHandler;
import com.haiking.utils.ParameterMapping;
import com.haiking.utils.ParameterMappingTokenHandler;
import com.haiking.vo.BoundSql;
import com.haiking.vo.Configuration;
import com.haiking.vo.MapperStatement;

public class SimpleExecutor implements Executor {

	@SuppressWarnings("unchecked")
	@Override
	public <E> List<E> query(Configuration configuration, MapperStatement mapperStatement, Object... params) throws Exception {
		//主从驱动，获取连接
		Connection connection = configuration.getDataSource().getConnection();
		//2.获取sql，//转换sql将#{} 转换为？
		String sql = mapperStatement.getSql();
		BoundSql boundSql = getBoundSql(sql);
		
		//获取预处理对象
		PreparedStatement prepareStatement = connection.prepareStatement(boundSql.getSqlText());
		//参数的对象的全路径
		String parameterType = mapperStatement.getParameterType();
		Class<?> parameterClass = getTypeClass(parameterType);
		//设置参数到prepareStatement中
		fillParameterMappingToPrepareStatement(boundSql, prepareStatement, parameterClass, params);
		//5.执行sql
		ResultSet resultSet = prepareStatement.executeQuery();
		String resultType = mapperStatement.getResultType();
		Class<?> resultClass = getTypeClass(resultType);
		List<Object> resultList = new ArrayList<Object>();
		//6、封装返回结果集
		while (resultSet.next()) {
			Object newInstance = resultClass.newInstance();
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				//字段的名称
				String columnName = metaData.getColumnName(i);
				//字段的值
				Object value = resultSet.getObject(columnName);
				
				//使用反射或者内省，根据数据库和实体的对应关系，完成封装
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultClass);
				Method writeMethod = propertyDescriptor.getWriteMethod();
				
				writeMethod.invoke(newInstance, value);
			}
			resultList.add(newInstance);
		}
		return (List<E>) resultList;
	}
	
	@Override
	public Integer delete(Configuration configuration, MapperStatement mapperStatement, Object... params)
			throws Exception {
		//1、主从驱动，获取连接
		Connection connection = configuration.getDataSource().getConnection();
		//2.获取sql，//转换sql将#{} 转换为？
		String sql = mapperStatement.getSql();
		BoundSql boundSql = getBoundSql(sql);
		//3、获取预处理对象
		PreparedStatement prepareStatement = connection.prepareStatement(boundSql.getSqlText());
		//参数的对象的全路径
		String parameterType = mapperStatement.getParameterType();
		Class<?> parameterClass = getTypeClass(parameterType);
		fillParameterMappingToPrepareStatement(boundSql, prepareStatement, parameterClass, params);
		int updateRows = prepareStatement.executeUpdate();
		//connection.commit();
		
		return updateRows;
	}

	private void fillParameterMappingToPrepareStatement(BoundSql boundSql, PreparedStatement prepareStatement,
			Class<?> parameterClass, Object... params)
			throws NoSuchFieldException, IllegalAccessException, SQLException {
		//4.设置参数
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		for (int i = 0; i < parameterMappings.size(); i++) {
			ParameterMapping parameterMapping = parameterMappings.get(i);
			String content = parameterMapping.getContent();
			
			//反射
			Field field = parameterClass.getDeclaredField(content);
			//暴力访问
			field.setAccessible(true);
			Object object = field.get(params[0]);
			prepareStatement.setObject(i+1, object);
		}
	}

	@Override
	public Integer insert(Configuration configuration, MapperStatement mapperStatement, Object... params)
			throws Exception {
			//1、主从驱动，获取连接
			Connection connection = configuration.getDataSource().getConnection();
			//2.获取sql，//转换sql将#{} 转换为？
			String sql = mapperStatement.getSql();
			BoundSql boundSql = getBoundSql(sql);
			String finalSql = boundSql.getSqlText();
			//解析后的sql缺少最后的半个括号
			finalSql = finalSql +")";
			//3、获取预处理对象
			PreparedStatement prepareStatement = connection.prepareStatement(finalSql);
			//参数的对象的全路径
			String parameterType = mapperStatement.getParameterType();
			Class<?> parameterClass = getTypeClass(parameterType);
			fillParameterMappingToPrepareStatement(boundSql, prepareStatement, parameterClass, params);
			return prepareStatement.executeUpdate();
	}

	@Override
	public Integer update(Configuration configuration, MapperStatement mapperStatement, Object... params)
			throws Exception {
		//1、主从驱动，获取连接
		Connection connection = configuration.getDataSource().getConnection();
		//2.获取sql，//转换sql将#{} 转换为？
		String sql = mapperStatement.getSql();
		BoundSql boundSql = getBoundSql(sql);
		//3、获取预处理对象
		PreparedStatement prepareStatement = connection.prepareStatement(boundSql.getSqlText());
		//参数的对象的全路径
		String parameterType = mapperStatement.getParameterType();
		Class<?> parameterClass = getTypeClass(parameterType);
		fillParameterMappingToPrepareStatement(boundSql, prepareStatement, parameterClass, params);
		return prepareStatement.executeUpdate();
	}

	private Class<?> getTypeClass(String parameterType) throws Exception {
		if (parameterType!=null) {
			Class<?> forName = Class.forName(parameterType);
			return forName;
		}
		return null;
	}

	private BoundSql getBoundSql(String sql) {
		ParameterMappingTokenHandler tokenHandler = new ParameterMappingTokenHandler();
		GenericTokenHandler genericTokenHandler = new GenericTokenHandler("#{", "}", tokenHandler);
		//获取解析后的sql
		String parseSql = genericTokenHandler.parse(sql);
		//解析后的参数
		List<ParameterMapping> parameterMappings = tokenHandler.getParameterMappings();
		
		BoundSql boundSql = new BoundSql(parseSql,parameterMappings);
		return boundSql;
	}

	

}
