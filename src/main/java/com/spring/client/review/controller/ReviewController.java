package com.spring.client.review.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.admin.space.domain.Space;
import com.spring.client.domain.Member;
import com.spring.client.review.domain.Review;
import com.spring.client.review.service.ReviewService;
import com.spring.client.auth.repository.UserAuthRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;
    private final UserAuthRepository userAuthRepository;

    @GetMapping(value = "/reviewList/{spaceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Review> reviewList(@PathVariable Long spaceId) {
        Space space = new Space();
        space.setSpNo(spaceId);
        Review review = new Review();
        review.setSpace(space);
        return reviewService.reviewList(review);
    }

    @PostMapping(value = "/reviewInsert", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Review> reviewInsert(@RequestBody Review review, HttpSession session) {
        String loggedInUserId = (String) session.getAttribute("loggedInUser");
        if (loggedInUserId != null) {
            Member member = userAuthRepository.findByMemberId(loggedInUserId);
            if (member != null) {
                review.setMember(member);
                Review savedReview = reviewService.reviewInsert(review);
                return ResponseEntity.ok(savedReview);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PutMapping(value = "/{reviewId}", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Review updateReview(@PathVariable Long reviewId, @RequestBody Review review) {
        review.setRevNo(reviewId);
        return reviewService.reviewUpdate(review);
    }
    
    @GetMapping("/getMemberId")
    @ResponseBody
    public ResponseEntity<String> getMemberId(HttpSession session) {
        String loggedInUserId = (String) session.getAttribute("loggedInUser");
        if (loggedInUserId != null) {
            Member member = userAuthRepository.findByMemberId(loggedInUserId);
            if (member != null) {
                return ResponseEntity.ok(member.getMemberId());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


    @DeleteMapping(value = "/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void reviewDelete(@PathVariable Long reviewId) {
        Review review = new Review();
        review.setRevNo(reviewId);
        reviewService.reviewDelete(review);
    }
}
