package com.spring.client.review.repository;

import com.spring.client.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.space.spNo = ?1")
    List<Review> findBySpNo(Long spNo);

    @Query("SELECT r FROM Review r WHERE r.reservation.resNo = ?1")
    List<Review> findByResNo(Long resNo);

}
