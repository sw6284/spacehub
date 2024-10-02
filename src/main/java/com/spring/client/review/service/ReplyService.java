package com.spring.client.review.service;

import java.util.List;

import com.spring.client.review.domain.Reply;

public interface ReplyService {

    /* 대댓글 리스트 */
    List<Reply> findRepliesByReviewId(Long reviewId);

    /* 대댓글 작성 */
    Reply saveReply(Reply reply);

    /* 대댓글 수정 */
    Reply updateReply(Long replyId, String replyContent);

    /* 대댓글 삭제 */
    void deleteReply(Long replyId);
    
    /* 특정 reviewId에 대한 대댓글 리스트 */
    List<Reply> getReplyList(Long reviewId);
}
