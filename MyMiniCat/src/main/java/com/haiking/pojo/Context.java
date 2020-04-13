package com.haiking.pojo;

import com.haiking.servlet.HttpServlet;
import com.haiking.servlet.Servlet;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private Servlet servlet;
    private Map<String, HttpServlet> servletMap;

    public Map<String, HttpServlet> getServletMap() {
        if (servletMap == null) {
            servletMap = new HashMap<>();
        }
        return servletMap;
    }

    public void setServletMap(Map<String, HttpServlet> servletMap) {
        this.servletMap = servletMap;
    }

    public Servlet getServlet() {
        return servlet;
    }

    public void setServlet(Servlet servlet) {
        this.servlet = servlet;
    }
}
