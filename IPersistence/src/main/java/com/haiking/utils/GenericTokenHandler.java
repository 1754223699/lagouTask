package com.haiking.utils;

public class GenericTokenHandler {
	private String openToken;//开始标记
	private String closeToken;//结束标记
	private final TokenHandler handler;//标记处理器
	public GenericTokenHandler(String openToken, String closeToken,TokenHandler handler){
		this.closeToken = closeToken;
		this.openToken = openToken;
		this.handler = handler;
	}
	
	public String parse(String sqlText){
		if (sqlText==null || sqlText.isEmpty()) {
			return "";
		}
		int start = sqlText.indexOf(openToken);
		if (start == -1) {
			return sqlText;
		}
		char[] src = sqlText.toCharArray();
		int offset = 0;
		final StringBuilder builder = new StringBuilder();
		StringBuilder expression = null;
		while (start >-1) {
			if (start >0 && src[start-1] == '\\') {
				builder.append(src,offset,start-offset-1).append(openToken);
				offset = start + openToken.length();
			}else{
				if (expression==null) {
					expression = new StringBuilder();
				}else {
					expression.setLength(0);
				}
				builder.append(src,offset,start-offset);
				offset = start + openToken.length();
				int end = sqlText.indexOf(closeToken,offset);
				while(end>-1){
					if (end>offset && src[end-1] == '\\') {
						expression.append(src,offset,end-offset-1).append(closeToken);
						offset = end +closeToken.length();
						end = sqlText.indexOf(closeToken,offset);
					}else{
						expression.append(src,offset,end-offset);
						offset = end +closeToken.length();
						break;
					}
				}
				if (end == -1) {
					builder.append(src,start,src.length - start);
					offset = src.length;
				}else{
					builder.append(handler.handleToken(expression.toString()));
					offset = end +closeToken.length();
				}
			}
			start = sqlText.indexOf(openToken,offset);
		}
		return builder.toString();
	}
}
