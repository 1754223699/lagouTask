package com.haiking.spring.boot.controller;

import com.haiking.spring.boot.pojo.Article;
import com.haiking.spring.boot.service.ArticleService;
import com.haiking.spring.boot.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @RequestMapping("/findById")
    public String findById(@RequestParam("id") Integer id, Model model){
        Article article = articleService.findById(id);
        model.addAttribute("article",article);
        System.out.println(article);
        return "index";
    }

    @RequestMapping("/findAllArticle")
    public String findAllArticle(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "pageSize",defaultValue = "2") Integer pageSize,
                                 Model model){
        Page<Article> pages = articleService.findAll(pageNum, pageSize);
        model.addAttribute("page",pages);
        return  "/client/index";
    }

}
