package cn.mrx.blog.service.impl;

import cn.mrx.blog.model.Vote;
import cn.mrx.blog.repository.VoteRepository;
import cn.mrx.blog.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: xialiangbo
 * Date: 2017/8/30 0:44
 * Description:
 */
@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public Vote getVoteById(Long id) {
        return voteRepository.findOne(id);
    }

    @Override
    public void removeVote(Long id) {
        voteRepository.delete(id);
    }
}
