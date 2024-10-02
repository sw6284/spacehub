package com.spring.client.review.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.admin.space.domain.Space;
import com.spring.client.domain.ClientAuthRepositoryTests;
import com.spring.client.domain.Member;
import com.spring.client.reservation.domain.Reservation;
import com.spring.client.review.domain.Review;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class reviewRepositoryTest {
	
	@Setter(onMethod_=@Autowired)
	private ReviewRepository reviewRepository;
	
	

	@Test
	public void reviewInsertTest() {
	    for (int i = 1; i <= 10; i++) {
	        Space space = new Space();
	        space.setSpNo((long) 2);

	        Member member = new Member();
	        member.setMemberNo((long) 1);

	        Reservation reservation = new Reservation();
	        reservation.setResNo((long) (2)); // 예시로 예약 번호를 다르게 설정

	        log.info("Reservation: " + reservation);
	        log.info("Reservation ID: " + reservation.getResNo());

	        Review review = new Review();
	        review.setSpace(space);
	        review.setMember(member);
	        review.setReservation(reservation);

	        // 리뷰 점수와 내용 다르게 설정
	        review.setRevScore(i % 5 + 1); // 1부터 5까지의 점수
	        review.setRevContent("리뷰 " + i + ": " + i + "명이 사용했는데 " + (i % 2 == 0 ? "공간이 넓고 쾌적했어요." : "공간이 조금 좁았어요.") + " 다만 " + (i % 3 == 0 ? "청결 상태가 아쉬웠어요." : "이용하기 편리했어요."));

	        review.setRevDate(LocalDateTime.now());
	        reviewRepository.save(review);
	    }
	}
	
	/*@Test
	public void reviewDeleteTest() {
		reviewRepository.deleteById(9L);
	}*/
	/*
	@Test
	public void SpNoSelectTest() {
		List<Review> reviewList = reviewRepository.findBySpNo(3L);
		for(Review review : reviewList) {
			log.info(review.toString());
		}
	}*/
	
	/*
	@Test
	public void reviewListTest() {
		List<Review> reivewList = (List<Review>) reviewRepository.findBySpNo(3L);
		for(Review review : reivewList) {
			
			log.info(review.toString());
		}
	}*/

}
