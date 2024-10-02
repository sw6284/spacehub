package com.spring.client.review.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.client.review.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByReviewRevNo(Long revNo);
}
