package com.haiking.servlet;

import com.haiking.pojo.Request;
import com.haiking.pojo.Response;

public interface Servlet {
    public void init() throws Exception;

    public void destroy() throws Exception;

    public void service(Request request, Response response) throws  Exception;

}
