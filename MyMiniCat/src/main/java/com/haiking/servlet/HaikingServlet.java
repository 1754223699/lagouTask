package com.haiking.servlet;

import com.haiking.pojo.Request;
import com.haiking.pojo.Response;
import com.haiking.util.HttpUtils;

public class HaikingServlet extends HttpServlet {
    @Override
    public void doGet(Request request, Response response) throws Exception {
        String content = "<h1>HaikingServlet demo1 doGet method <h1>";
        response.output(HttpUtils.getHttpHeader200((long) content.getBytes().length) + content);
    }

    @Override
    public void doPost(Request request, Response response) throws Exception {
        String content = "<h1>HaikingServlet doPost method <h1>";
        response.output(HttpUtils.getHttpHeader200((long) content.getBytes().length) + content);
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void destroy() throws Exception {

    }
}
