package com.haiking.pojo;

import java.io.IOException;
import java.io.InputStream;

public class Request {
    //请求方式
    private String method;
    //请求路径
    private String url;
    //输入流，其他属性从输入流中解析出来
    private InputStream inputStream;

    public Request() {
    }

    public Request(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        int count = 0;
        while (count==0){
            count = inputStream.available();
        }

        byte[] bytes = new byte[count];
        inputStream.read(bytes);
        String  inputString = new String(bytes);

        String  firstLine =  inputString.split("\\n")[0];
        String[] strings = firstLine.split(" ");
        this.method = strings[0];
        this.url = strings[1];
        System.out.println("this method is :" +method);
        System.out.println("this url is :" +url);
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
