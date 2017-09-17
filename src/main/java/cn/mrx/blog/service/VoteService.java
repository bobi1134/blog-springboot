package cn.mrx.blog.service;

import cn.mrx.blog.model.Vote;

/**
 * Author: xialiangbo
 * Date: 2017/8/30 0:43
 * Description:
 */
public interface VoteService {

    /**
     * 根据id获取 Vote
     * @param id
     * @return
     */
    Vote getVoteById(Long id);
    /**
     * 删除Vote
     * @param id
     * @return
     */
    void removeVote(Long id);
}
