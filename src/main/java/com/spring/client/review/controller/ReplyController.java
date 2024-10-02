package com.spring.client.review.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.client.review.domain.Reply;
import com.spring.client.review.service.ReplyService;

import jakarta.servlet.http.HttpSession;

import com.spring.admin.domain.Admin;
import com.spring.admin.auth.service.AdminAuthService;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReplyController {
	
	 // 특정 reviewId에 대한 답글 목록을 반환하는 새로운 메서드
    @GetMapping(value = "/replyList/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Reply>> getReplyList(@PathVariable Long reviewId) {
        List<Reply> replies = replyService.findRepliesByReviewId(reviewId);
        if (replies != null && !replies.isEmpty()) {
            return ResponseEntity.ok(replies);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

    private final ReplyService replyService;
    private final AdminAuthService adminAuthService;

    @PostMapping(value = "/replyInsert", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Reply insertReply(@RequestBody Reply reply) {
        return replyService.saveReply(reply);
    }

    @GetMapping(value = "/replies/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Reply> getReplies(@PathVariable Long reviewId) {
        return replyService.findRepliesByReviewId(reviewId);
    }

    @PutMapping(value = "/reply/{replyId}", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Reply updateReply(@PathVariable Long replyId, @RequestBody Reply reply) {
        return replyService.updateReply(replyId, reply.getReplyContent());
    }

    @DeleteMapping(value = "/reply/{replyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteReply(@PathVariable Long replyId) {
        replyService.deleteReply(replyId);
    }

    @GetMapping("/getAdminInfo")
    @ResponseBody
    public ResponseEntity<Admin> getAdminInfo(HttpSession session) {
        String loggedInAdmin = (String) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin != null) {
            Admin admin = adminAuthService.getAdminById(loggedInAdmin);
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
