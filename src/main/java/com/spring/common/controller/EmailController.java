package com.spring.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.common.service.EmailService;

import jakarta.mail.MessagingException;

@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-email")
    public String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String content) {
        try {
            emailService.sendEmail(to, subject, content);
            return "email_sent_success"; // 이메일 전송 성공 페이지
        } catch (MessagingException e) {
            e.printStackTrace();
            return "email_sent_failure"; // 이메일 전송 실패 페이지
        }
    }
}
