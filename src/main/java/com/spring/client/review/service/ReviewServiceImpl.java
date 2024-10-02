package com.spring.client.review.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.client.review.domain.Review;
import com.spring.client.review.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public List<Review> findBySpNo(Long spNo) {
        return reviewRepository.findBySpNo(spNo);
    }

    @Override
    public List<Review> reviewList(Review review) {
        return reviewRepository.findBySpNo(review.getSpace().getSpNo());
    }

    @Override
    public Review reviewInsert(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public Review reviewUpdate(Review review) {
        Optional<Review> reviewOptional = reviewRepository.findById(review.getRevNo());
        if (reviewOptional.isPresent()) {
            Review updateReview = reviewOptional.get();
            updateReview.setRevScore(review.getRevScore());
            updateReview.setRevContent(review.getRevContent());
            return reviewRepository.save(updateReview);
        } else {
            throw new RuntimeException("Review not found with id " + review.getRevNo());
        }
    }

    @Override
    public void reviewDelete(Review review) {
        reviewRepository.deleteById(review.getRevNo());
    }

	@Override
	public List<Review> findByResNo(Long resNo) {
        return reviewRepository.findByResNo(resNo);

	}
}
