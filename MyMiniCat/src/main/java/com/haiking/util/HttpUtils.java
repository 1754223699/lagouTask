package com.haiking.util;

public class HttpUtils {

    /**
     * 状态码为200
     *
     * @param contentLeagth
     * @return
     */
    public static String getHttpHeader200(Long contentLeagth) {
        return "HTTP/1.1 200 OK \n" + "Content-Type: text/html \n" +
                "Content-Length: " + contentLeagth + " \n" + "\r\n";
    }

    public static String getHttpHeader404() {
        String string404 = "<h1>404<h1>";
        return "HTTP/1.1 404 NOT FOUND \n" + "Content-Type: text/html" +
                "Content-Length: " + string404.getBytes().length + " \n" + "\r\n" + string404;
    }
}
