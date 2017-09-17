package cn.mrx.blog.service;

import cn.mrx.blog.model.Authority;

/**
 * Author: xialiangbo
 * Date: 2017/8/23 22:53
 * Description:
 */
public interface AuthorityService {

    /**
     * 根据id获取 Authority
     * @param id
     * @return
     */
    Authority getAuthorityById(Long id);
}
