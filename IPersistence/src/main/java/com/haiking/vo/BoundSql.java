package com.haiking.vo;

import java.util.List;

import com.haiking.utils.ParameterMapping;

public class BoundSql {
    private String sqlText;
    private List<ParameterMapping> parameterMappings;

    public BoundSql(String parseSql, List<ParameterMapping> parameterMappings) {
        this.sqlText = parseSql;
        this.parameterMappings = parameterMappings;
    }

    public String getSqlText() {
        return sqlText;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void setParameterMappings(List<ParameterMapping> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }

}
