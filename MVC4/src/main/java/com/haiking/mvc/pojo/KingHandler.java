package com.haiking.mvc.pojo;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class KingHandler {

	private Object controller;
	private Method method ;
	private Pattern pattern;
	private Map<String, Integer> parameterIndexMapping;
	private List<String> whitelist;

	public KingHandler(Object value, Method method, Pattern compile) {
		this.method = method;
		this.controller = value;
		this.pattern = compile;
		this.parameterIndexMapping = new HashMap<String, Integer>();
	}
	public Object getController() {
		return controller;
	}
	public void setController(Object controller) {
		this.controller = controller;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public Pattern getPattern() {
		return pattern;
	}
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
	public Map<String, Integer> getParameterIndexMapping() {
		return parameterIndexMapping;
	}
	public void setParameterIndexMapping(Map<String, Integer> parameterIndexMapping) {
		this.parameterIndexMapping = parameterIndexMapping;
	}
	public List<String> getWhitelist() {
		return whitelist;
	}
	public void setWhitelist(List<String> whitelist) {
		this.whitelist = whitelist;
	}
}
