package com.haiking.io;

import java.io.InputStream;

public class Resources {
	public static InputStream getResourcesAsStream(String path){
		InputStream inputStream = Resources.class.getResourceAsStream(path);
		return inputStream;
	}
}
