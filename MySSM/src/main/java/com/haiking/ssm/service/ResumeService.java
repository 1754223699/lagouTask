package com.haiking.ssm.service;

import com.haiking.ssm.pojo.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ResumeService {
    Page<Resume> findAllResume(Pageable pageable) throws Exception;
}
