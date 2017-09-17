package cn.mrx.blog.repository;

import cn.mrx.blog.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author: xialiangbo
 * Date: 2017/8/30 0:43
 * Description:
 */
public interface VoteRepository extends JpaRepository<Vote, Long>{
}
