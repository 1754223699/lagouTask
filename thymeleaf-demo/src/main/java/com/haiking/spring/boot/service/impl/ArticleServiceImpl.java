package com.haiking.spring.boot.service.impl;

import com.haiking.spring.boot.dao.ArticleRepository;
import com.haiking.spring.boot.pojo.Article;
import com.haiking.spring.boot.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Article findById(Integer id){
        Optional<Article> byId = articleRepository.findById(id);
        if (byId.isPresent()){
            return  byId.get();
        }
        return  null;
    }

    @Override
    public  Page<Article> findAll(int page, int size){
         Page<Article> articlePages = articleRepository.findAll( PageRequest.of(page-1,size, Sort.Direction.DESC,"id"));
         return  articlePages;
    }

}
