package com.haiking.spring.boot.dao;

import com.haiking.spring.boot.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ArticleRepository extends JpaRepository<Article,Integer> , JpaSpecificationExecutor<Article> {
    public Page<Article> findAll( Pageable pageable);
}
