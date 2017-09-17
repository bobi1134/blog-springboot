package cn.mrx.blog.service.impl;

import cn.mrx.blog.model.Comment;
import cn.mrx.blog.repository.CommentRepository;
import cn.mrx.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: xialiangbo
 * Date: 2017/8/29 23:13
 * Description:
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findOne(id);
    }

    @Override
    public void removeComment(Long id) {
        commentRepository.delete(id);
    }
}
