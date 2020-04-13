package com.haiking.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StaticResponseUtils {
    /**
     * 获取静态资源文件的绝对路径
     *
     * @param path
     * @return
     */
    public static String getAbsolutePath(String path) {
        String absolutePath = StaticResponseUtils.class.getResource("/").getPath();
        return absolutePath.replaceAll("\\\\", "/") + path;
    }

    public static void outputStaticResouse(InputStream inputStream, OutputStream outputStream) throws IOException {
        int count = 0;
        while (count == 0) {
            count = inputStream.available();
        }
        int resourceSize = count;
        //输出http请求头，然后再输出具体内容
        outputStream.write(HttpUtils.getHttpHeader200((long) resourceSize).getBytes());
        //读取内容输出
        long written = 0;//已经读取的内容长度
        int byteSize = 1024;//计划每次缓存的长度
        byte[] bytes = new byte[byteSize];
        while (written < resourceSize) {
            //说明剩余未读大小不足1024长度，那么就按真实长度来处理
            if (written + byteSize > resourceSize) {
                //剩余的文件内容的长度
                byteSize = (int) (resourceSize - written);
                bytes = new byte[byteSize];
            }
            inputStream.read(bytes);
            outputStream.write(bytes);
            outputStream.flush();
            written += byteSize;
        }
    }

}
