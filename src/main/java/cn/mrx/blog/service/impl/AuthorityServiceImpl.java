package cn.mrx.blog.service.impl;

import cn.mrx.blog.model.Authority;
import cn.mrx.blog.repository.AuthorityRepository;
import cn.mrx.blog.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: xialiangbo
 * Date: 2017/8/23 22:53
 * Description:
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Authority getAuthorityById(Long id) {
        return authorityRepository.findOne(id);
    }

}
