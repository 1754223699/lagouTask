package com.haiking.spring.boot.service;

import com.haiking.spring.boot.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ArticleService {
    public Article findById(Integer id);
    public  Page<Article> findAll(int page, int size);
}
