package com.haiking.pojo;

import com.haiking.util.HttpUtils;
import com.haiking.util.StaticResponseUtils;

import java.io.*;

public class Response {
    private OutputStream outputStream;

    public Response() {
    }

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public  void  output(String content) throws IOException {
        outputStream.write(content.getBytes());
    }

    public void  outputHtml(String path) throws IOException {
        String absolutePath = StaticResponseUtils.getAbsolutePath(path);
        File file = new File(absolutePath);
        if (file.exists() && file.isFile()){
            //读取静态资源文件，输出静态资源
            StaticResponseUtils.outputStaticResouse(new FileInputStream(file),outputStream);
        }else{
            output(HttpUtils.getHttpHeader404());
        }
    }
}
