package com.haiking.vo;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

public class Configuration {
	//数据源
	private DataSource dataSource;
	//key :statementId
	private Map<String, MapperStatement> mapperStatementMap = new HashMap<>();
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public Map<String, MapperStatement> getMapperStatementMap() {
		return mapperStatementMap;
	}
	public void setMapperStatementMap(Map<String, MapperStatement> mapperStatementMap) {
		this.mapperStatementMap = mapperStatementMap;
	}
}
