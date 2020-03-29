package com.haiking.mvc.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.haiking.mvc.annotation.*;
import org.apache.commons.lang3.StringUtils;

import com.haiking.mvc.pojo.KingHandler;

public class HaikingDispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 9153580562670259111L;

    private Properties properties = new Properties();
    private List<String> classNames = new ArrayList<String>();
    private Map<String, Object> iocMap = new HashMap<>();
    private List<KingHandler> handlerMapping = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        //1、加载配置类文件springmvc.properties contextConfigLocation
        String configLocation = config.getInitParameter("contextConfigLocation");
        doLoadConfig(configLocation);
        //2.扫描相关的类，扫描注解
        String scanPackage = (String) properties.get("scanPackage");

        doScan(scanPackage);
        //3、初始化对象，实现IOC
        doInstance();
        //4、实现依赖注入
        doAutoWired();
        //5、构造一个HandlerMapping映射处理器
        initHandlerMapping();

        System.out.println("mySpringmvc init completed");

    }

    //5、构造一个HandlerMapping映射处理器
    private void initHandlerMapping() {
        if (iocMap.isEmpty()) {
            return;
        }
        for (Entry<String, Object> entry : iocMap.entrySet()) {
            Class<? extends Object> class1 = entry.getValue().getClass();
            if (!class1.isAnnotationPresent(KingController.class)) {
                continue;
            }
            String baseUrl = "";
            if (class1.isAnnotationPresent(KingRequestMapping.class)) {
                KingRequestMapping kingRequestMapping = class1.getAnnotation(KingRequestMapping.class);
                baseUrl = kingRequestMapping.value();
            }
            String[] classWhitelist = new String[0];
            if (class1.isAnnotationPresent(Security.class)) {
                Security security = class1.getAnnotation(Security.class);
                String[] baseNames = security.value();
                if (baseNames.length > 0) {
                    classWhitelist = baseNames;
                }
            }

            //获取方法上的mapping注解
            Method[] methods = class1.getMethods();
            for (Method method : methods) {
                List<String> list = new ArrayList<>(0);
                List<String> whiteList = Arrays.asList(classWhitelist);
                list.addAll(whiteList);
                KingHandler handler = new KingHandler(entry.getValue(), method, Pattern.compile(baseUrl));
                if (method.isAnnotationPresent(KingRequestMapping.class)) {
                    KingRequestMapping kingRequestMapping = method.getAnnotation(KingRequestMapping.class);
                    String value = kingRequestMapping.value();
                    String finalMethodUrl = baseUrl + value;
                    handler = new KingHandler(entry.getValue(), method, Pattern.compile(finalMethodUrl));
                    // 计算方法的参数位置信息
                    Parameter[] parameters = method.getParameters();
                    for (int j = 0; j < parameters.length; j++) {
                        Parameter parameter = parameters[j];

                        if (parameter.getType() == HttpServletRequest.class || parameter.getType() == HttpServletResponse.class) {
                            // 如果是request和response对象，那么参数名称写HttpServletRequest和HttpServletResponse
                            handler.getParameterIndexMapping().put(parameter.getType().getSimpleName(), j);
                        } else {
                            handler.getParameterIndexMapping().put(parameter.getName(), j);  // <name,2>
                        }
                    }

                    if (method.isAnnotationPresent(Security.class)) {
                        Security security = method.getAnnotation(Security.class);
                        String[] methodWhiteList = security.value();
                        if (methodWhiteList.length > 0) {
                            List<String> strings = Arrays.asList(methodWhiteList);
                            list.addAll(strings);
                            handler.setWhitelist(list);
                        }
                    }
                } else {
                    handler.setWhitelist(list);
                }
                // 建立url和method之间的映射关系（map缓存起来）
                handlerMapping.add(handler);
            }
        }
    }

    //4、实现依赖注入
    private void doAutoWired() {
        if (iocMap.isEmpty()) {
            return;
        }
        for (Entry<String, Object> entry : iocMap.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(KingAutowired.class)) {
                    continue;
                }
                KingAutowired kingAutowired = field.getAnnotation(KingAutowired.class);
                String name = kingAutowired.value();
                if ("".equals(name.trim())) {
                    name = field.getType().getName();
                }
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(), iocMap.get(name));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    //3、初始化对象，实现IOC
    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }
        for (int i = 0; i < classNames.size(); i++) {
            String className = classNames.get(i);
            try {
                Class<?> forName = Class.forName(className);
                if (forName.isAnnotationPresent(KingController.class)) {
                    String simpleName = forName.getSimpleName();
                    String lowerFirstSimpleName = lowerFirstChar(simpleName);
                    Object newInstance = forName.newInstance();
                    iocMap.put(lowerFirstSimpleName, newInstance);
                } else if (forName.isAnnotationPresent(KingService.class)) {
                    KingService kingService = forName.getAnnotation(KingService.class);
                    String beanName = kingService.value();
                    if (!"".equals(beanName.trim())) {
                        iocMap.put(beanName, forName.newInstance());
                    } else {
                        beanName = lowerFirstChar(forName.getSimpleName());
                        iocMap.put(beanName, forName.newInstance());
                    }
                    Class<?>[] interfaces = forName.getInterfaces();
                    for (Class<?> class1 : interfaces) {
                        iocMap.put(class1.getName(), forName.newInstance());
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    private String lowerFirstChar(String simpleName) {
        char[] chars = simpleName.toCharArray();
        if ('A' <= chars[0] && chars[0] <= 'Z') {
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }

    //2.扫描相关的类，扫描注解
    private void doScan(String scanPackage) {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String packagePath = scanPackage.replaceAll("\\.", "/");
        String scanPackagePath = path + packagePath;
        if (scanPackagePath.startsWith("/")) {
            scanPackagePath = scanPackagePath.substring(1);
        }
        File pack = new File(scanPackagePath);
        File[] listFiles = pack.listFiles();
        if (listFiles == null) {
            return;
        }
        for (File file : listFiles) {
            if (file.isDirectory()) {
                doScan(scanPackage + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = scanPackage + "." + file.getName().replace(".class", "");
                classNames.add(className);
            }
        }

    }

    //加载配置文件
    private void doLoadConfig(String configLocation) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(configLocation);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //根据URI获取到能够处理当前请求的uri
        KingHandler handler = getHandler(req);
        if (handler == null) {
            resp.getWriter().write("404 not found this uri");
            return;
        }
        Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();
        Object[] parasValue = new Object[parameterTypes.length];

        Map<String, String[]> parameterMap = req.getParameterMap();
        for (Entry<String, String[]> entry : parameterMap.entrySet()) {
            String value = StringUtils.join(entry.getValue(), ",");
            if (!handler.getParameterIndexMapping().containsKey(entry.getKey())) {
                continue;
            }
            Integer index = handler.getParameterIndexMapping().get(entry.getKey());
            parasValue[index] = value;
        }
        int requestIndex = handler.getParameterIndexMapping().get(HttpServletRequest.class.getSimpleName());
        parasValue[requestIndex] = req;

        int responseIndex = handler.getParameterIndexMapping().get(HttpServletResponse.class.getSimpleName());
        parasValue[responseIndex] = resp;
        String[] names = parameterMap.get("name");
        List<String> nameList = Arrays.asList(names);
        if (!handler.getWhitelist().containsAll(nameList)) {
            resp.getWriter().write("error you don't have right");
            return;
        }
        try {
            handler.getMethod().invoke(handler.getController(), parasValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private KingHandler getHandler(HttpServletRequest req) {
        if (handlerMapping.isEmpty()) {
            return null;
        }

        String url = req.getRequestURI();

        for (KingHandler handler : handlerMapping) {
            Matcher matcher = handler.getPattern().matcher(url);
            if (!matcher.matches()) {
                continue;
            }
            return handler;
        }
        return null;
    }

}
