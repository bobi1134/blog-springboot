package cn.mrx.blog.repository;

import cn.mrx.blog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: xialiangbo
 * Date: 2017/8/29 23:12
 * Description:
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
