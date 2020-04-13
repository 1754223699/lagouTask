package com.haiking;

import com.haiking.pojo.Mapper;
import com.haiking.pojo.Request;
import com.haiking.pojo.Response;
import com.haiking.pojo.Wrapper;
import com.haiking.servlet.HttpServlet;
import com.haiking.servlet.Servlet;
import com.haiking.util.HttpUtils;
import com.haiking.util.RequestProcessor;
import com.haiking.util.StaticResponseUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class BootStrap {
    /**
     * 定义监听的端口
     */
    private int listeanPort = 8080;
    private Map<String, HttpServlet> servletMap = new HashMap<>();

    public int getListeanPort() {
        return listeanPort;
    }

    public void setListeanPort(int listeanPort) {
        this.listeanPort = listeanPort;
    }

    public static void main(String[] args) {
        BootStrap bootStrap = new BootStrap();
        try {
            bootStrap.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void start() throws Exception {
        ServerSocket serverSocket = new ServerSocket(listeanPort);
        System.out.println("================>>MyMiniCat start on port:" + listeanPort);
        //加载web.xml
        loadServlet();

        //加载server.xml
        loadServer();

        int corePoolSize = 10;
        int maximumPoolSize = 50;
        long keepAliveTime = 100L;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(50);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = new
                ThreadPoolExecutor.AbortPolicy();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                unit, workQueue, threadFactory, handler);
        /**
         * miniCat 1.0
         */
        /*while (true) {
            Socket socket = serverSocket.accept();
            OutputStream outputStream = socket.getOutputStream();
            String outputData = "Helle World";
            String finalData = HttpUtils.getHttpHeader200((long) outputData.getBytes().length) + outputData;
            outputStream.write(finalData.getBytes());
            socket.close();
        }*/

        /**
         * MyMinicat 2.0
         */
        /*while (true) {
            Socket accept = serverSocket.accept();
            InputStream inputStream = accept.getInputStream();
            Request request = new Request(inputStream);
            Response response = new Response(accept.getOutputStream());
            response.outputHtml(request.getUrl());
            accept.close();
        }*/
        while (true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            // 封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            // 静态资源处理
            if (servletMap.get(request.getUrl()) == null && mapper.getHost().getContext().getServletMap().get(request.getUrl()) == null) {
                response.outputHtml(request.getUrl());
            } else if (servletMap.get(request.getUrl()) != null) {
                // 动态资源servlet请求
                HttpServlet httpServlet = servletMap.get(request.getUrl());
                httpServlet.service(request, response);
            } else {
                // 动态资源servlet请求
                HttpServlet httpServlet = mapper.getHost().getContext().getServletMap().get(request.getUrl());
                httpServlet.service(request, response);
            }
            socket.close();
        }
//        while (true) {
//            Socket accept = serverSocket.accept();
//            RequestProcessor requestProcessor = new RequestProcessor(accept, servletMap);
//            RequestProcessor requestProcessor2 = new RequestProcessor(accept, mapper.getHost().getContext().getServletMap());
//            InputStream inputStream = accept.getInputStream();
//            Request request = new Request(inputStream);
//            if (servletMap.containsKey(request.getUrl())) {
//                threadPoolExecutor.execute(requestProcessor);
//            } else {
//                threadPoolExecutor.execute(requestProcessor2);
//            }
//        }
    }

    private void loadServlet() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(inputStream);
            Element rootElement = document.getRootElement();
            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (int i = 0; i < selectNodes.size(); i++) {
                Element element = selectNodes.get(i);
                Element servletNameElement = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletNameElement.getStringValue();
                Element servletClassElement = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletClassElement.getStringValue();
                //根据Servlet-name的值找到url-pattern
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/" +
                        "servlet-mapping[servlet-name='" + servletName + "']");
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();

                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Mapper mapper = new Mapper();
    private Map<String, String> classMap = new HashMap<>();

    private void loadServer() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("server.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(inputStream);
            Element rootElement = document.getRootElement();
            Element connectorElement = (Element) rootElement.selectSingleNode("//Connector");
            String listentPort = connectorElement.attributeValue("port");
            System.out.println(listentPort);
            Element engineElement = (Element) connectorElement.selectSingleNode("//Engine");
            Element hostElement = (Element) engineElement.selectSingleNode("//Host");
            //localhost
            String name = hostElement.attributeValue("name");
            ///Users/webapps
            String appBase = hostElement.attributeValue("appBase");

            URL resource = this.getClass().getClassLoader().getResource("");
            String path = resource.getPath();
            String subPath = path.substring(0, path.indexOf("target") - 1);
            File file = new File(subPath + appBase);
            if (file.exists() && file.isDirectory()) {
                loadAppFile(file);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAppFile(File file) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        File[] files = file.listFiles();
        String appName = "";
        String url = "";
        for (File f : files) {
            if (f.isDirectory()) {
                appName = f.getName();
                url = loadChildFile(f, appName);
                mapper.getHost().getContext().getServletMap().put(url, (HttpServlet) Class.forName(classMap.get("")).newInstance());
            }
        }
    }

    private String loadChildFile(File file, String appName) {
        File[] files = file.listFiles();
        String url = null;
        for (File f : files) {
            if (f.getName().equals("web.xml")) {
                SAXReader saxReader = new SAXReader();
                try {
                    Document document = saxReader.read(f);
                    Element rootElement = document.getRootElement();
                    List<Element> selectNodes = rootElement.selectNodes("//servlet");
                    for (int i = 0; i < selectNodes.size(); i++) {
                        Element element = selectNodes.get(i);
                        Element servletNameElement = (Element) element.selectSingleNode("servlet-name");
                        String servletName = servletNameElement.getStringValue();
                        Element servletClassElement = (Element) element.selectSingleNode("servlet-class");
                        String servletClass = servletClassElement.getStringValue();
                        //根据Servlet-name的值找到url-pattern
                        Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/" +
                                "servlet-mapping[servlet-name='" + servletName + "']");
                        String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                        if (null != appName) {
                            if (urlPattern.startsWith("/")) {
                                urlPattern = "/" + appName + urlPattern;
                            } else {
                                urlPattern = "/" + appName + "/" + urlPattern;
                            }
                        }
                        mapper.getHost().getContext().getServletMap().put(urlPattern,(HttpServlet) Class.forName(servletClass).newInstance());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (f.getName().endsWith(".class")) {
                classMap.put(f.getName().substring(0, f.getName().indexOf(".class")), f.getName());
            } else if (f.isDirectory()) {
                loadChildFile(f, appName);
            }
        }
        return url;
    }
}
