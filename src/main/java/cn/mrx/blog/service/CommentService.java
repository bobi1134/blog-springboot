package cn.mrx.blog.service;

import cn.mrx.blog.model.Comment;

/**
 * Author: xialiangbo
 * Date: 2017/8/29 23:13
 * Description:
 */
public interface CommentService {

    /**
     * 根据id获取评论
     * @param id
     * @return
     */
    Comment getCommentById(Long id);

    /**
     * 根据id删除评论
     * @param id
     */
    void removeComment(Long id);
}
