package com.haiking.servlet;

import com.haiking.pojo.Request;
import com.haiking.pojo.Response;

public abstract class HttpServlet implements Servlet {

    @Override
    public void service(Request request, Response response) throws Exception {
        if ("GET".equals(request.getMethod())){
            doGet(request,response);
        }else{
            doPost(request,response);
        }
    }

    public abstract void doGet(Request request, Response response) throws Exception;

    public abstract void doPost(Request request, Response response) throws Exception;
}
