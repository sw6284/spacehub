package com.spring.client.review.service;

import java.util.List;

import com.spring.client.review.domain.Review;

public interface ReviewService {
	
    List<Review> findBySpNo(Long spNo);
    
    List<Review> findByResNo(Long resNo);

    
	/* 리뷰 리스트 */
	public List<Review> reviewList(Review review);

	/* 리뷰 작성 */
	public Review reviewInsert(Review review);

	
	/* 리뷰 상세 페이지 */
	
	public Review reviewUpdate(Review review);
	
	public void reviewDelete(Review review);


	
	
	

}
