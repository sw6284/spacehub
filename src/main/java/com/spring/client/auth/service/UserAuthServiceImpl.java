package com.spring.client.auth.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.spring.client.auth.repository.UserAuthRepository;
import com.spring.client.domain.Member;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private final UserAuthRepository userAuthRepository;
    private final JavaMailSender mailSender;

    private final Map<String, VerificationCodeEntry> verificationCodes = new HashMap<>();

    @Override
    public void saveMember(Member member) {
        // 비밀번호 암호화 없이 평문 비밀번호 저장
        userAuthRepository.save(member);
    }

    @Override
    public boolean isIdAvailable(String memberId) {
        return !userAuthRepository.existsByMemberId(memberId);
    }
    
    @Override
    public boolean isEmailAvailable(String memberEmail) {
    	return !userAuthRepository.existsByMemberEmail(memberEmail);
    }
    @Override
    public Member userLogin(String memberId, String memberPassword) {
        Member member = userAuthRepository.findByMemberId(memberId);
        if (member != null && memberPassword.equals(member.getMemberPassword())) { // 평문 비밀번호 검증
            return member;
        }
        return null;
    }

    @Override
    public void sendVerificationCode(String email) {
        String verificationCode = generateVerificationCode();

        try {
            sendEmail(email, "이메일 인증번호", "인증번호: " + verificationCode);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        verificationCodes.put(email, new VerificationCodeEntry(verificationCode, System.currentTimeMillis()));
    }

    @Override
    public boolean verifyCode(String email, String code) {
        VerificationCodeEntry entry = verificationCodes.get(email);
        if (entry == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - entry.getTimestamp();
//        if (timeElapsed > TimeUnit.MINUTES.toMillis(5)) {
//            verificationCodes.remove(email);
//            return false;
//        }

        return entry.getCode().equals(code);
    }

    @Override
    public String findIdByNameAndEmail(String name, String email) {
        Member member = userAuthRepository.findByMemberNameAndMemberEmail(name, email);
        return (member != null) ? member.getMemberId() : null;
    }

    @Override
    public boolean resetPassword(String memberId, String email) {
        Member member = userAuthRepository.findByMemberId(memberId);
        if (member != null && member.getMemberEmail().equals(email)) {
            String tempPassword = generateTemporaryPassword();
            member.setMemberPassword(tempPassword); // 임시 비밀번호 저장 (암호화 없이)
            userAuthRepository.save(member);

            // 임시 비밀번호를 이메일로 전송
            try {
                sendEmail(email, "임시 비밀번호", "임시 비밀번호: " + tempPassword);
            } catch (MessagingException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    private void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("spacehubProj@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        mailSender.send(message);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return Integer.toString(code);
    }

    private String generateTemporaryPassword() {
        Random random = new Random();
        int length = 10;
        StringBuilder sb = new StringBuilder(length);
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    private static class VerificationCodeEntry {
        private final String code;
        private final long timestamp;

        public VerificationCodeEntry(String code, long timestamp) {
            this.code = code;
            this.timestamp = timestamp;
        }

        public String getCode() {
            return code;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
    
    @Override
    public Member getMemberById(String memberId) {
    	return userAuthRepository.findByMemberId(memberId);
    }
}
