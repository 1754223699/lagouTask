package com.haiking.ssm.service.impl;

import com.haiking.ssm.dao.ResumeDao;
import com.haiking.ssm.pojo.Resume;
import com.haiking.ssm.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ResumeDao resumeDao;
    @Override
    public Page<Resume> findAllResume(Pageable pageable) throws Exception {
        return resumeDao.findAll( pageable);
    }
}
