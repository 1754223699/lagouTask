package com.haiking.ssm.controller;

import com.haiking.ssm.pojo.Resume;
import com.haiking.ssm.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/resume")
public class ResumerController {

    @Autowired
    private ResumeService resumeService;


    @RequestMapping("/findAllResume")
    public ModelAndView findAllResume(@RequestParam Integer pageNum,@RequestParam Integer pageSize) throws Exception {
        Pageable pageable = PageRequest.of(pageNum,pageSize);
        Page<Resume> resumes = resumeService.findAllResume(pageable);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("listresume");
        mv.addObject("resumes",resumes);
        return  mv;
    }
}
