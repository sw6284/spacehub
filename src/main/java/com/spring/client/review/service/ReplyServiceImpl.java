package com.spring.client.review.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.client.review.domain.Reply;
import com.spring.client.review.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;
    
    
    @Override
    @Transactional(readOnly = true)
    public List<Reply> getReplyList(Long reviewId) {
        return replyRepository.findByReviewRevNo(reviewId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Reply> findRepliesByReviewId(Long reviewId) {
        return replyRepository.findByReviewRevNo(reviewId);
    }

    @Override
    @Transactional
    public Reply saveReply(Reply reply) {
        return replyRepository.save(reply);
    }

    @Override
    @Transactional
    public Reply updateReply(Long replyId, String replyContent) {
        Optional<Reply> replyOptional = replyRepository.findById(replyId);
        Reply updateReply = replyOptional.orElseThrow(() -> new IllegalArgumentException("Invalid reply ID"));
        updateReply.setReplyContent(replyContent);
        return replyRepository.save(updateReply);
    }

    @Override
    @Transactional
    public void deleteReply(Long replyId) {
        replyRepository.deleteById(replyId);
    }
}

