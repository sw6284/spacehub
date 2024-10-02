package com.spring.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void sendEmail(String to, String subject, String content) throws MessagingException {
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true);
	
	    helper.setFrom("spacehubProj@gmail.com"); // 본인의 Gmail 주소
	    helper.setTo(to);
	    helper.setSubject(subject);
	    helper.setText(content, true);
	
	    mailSender.send(message);
	}
}
