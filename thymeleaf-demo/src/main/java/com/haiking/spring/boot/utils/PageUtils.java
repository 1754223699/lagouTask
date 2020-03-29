package com.haiking.spring.boot.utils;

import java.util.HashMap;
import java.util.Map;

public class PageUtils {
    public static Map<String,Integer> pageNum(Integer pageNum,Integer totalPages,Integer pageSize){
        Map<String,Integer> pageMap = new HashMap<>(2);
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (totalPages != null) {
            if (pageNum > totalPages) {
                pageNum = totalPages;
            }
        }
        if(pageSize == null){
            pageSize = 1 ;
        }
        pageMap.put("pageNum", pageNum);
        pageMap.put("pageSize", pageSize);
        return pageMap;
    }

}
