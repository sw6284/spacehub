package com.spring.client.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.client.auth.service.UserAuthService;
import com.spring.client.domain.Member;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth/*")
public class UserAuthController {

    private final UserAuthService userAuthService;
    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;


    /**
     * 회원가입 페이지 반환
     * @param member 회원가입 폼 데이터 바인딩용 객체
     * @return 회원가입 페이지 뷰 이름
     */
    @GetMapping("/signupForm")
    public String signupForm(Member member) {
        return "client/auth/signupForm";
    }

    /**
     * 아이디 중복 체크
     * @param request 아이디를 포함한 요청 본문
     * @return 아이디 사용 가능 여부
     */
    @PostMapping("/checkId")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkId(@RequestBody Map<String, String> request) {
        String memberId = request.get("memberId");
        boolean available = userAuthService.isIdAvailable(memberId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", available);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 이메일 중복 체크
     * @param request 이메일을 포함한 요청 본문
     * @return 이메일 사용 가능 여부
     */
    @PostMapping("/checkEmail")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestBody Map<String, String> request) {
        String memberEmail = request.get("memberEmail");
        boolean available = userAuthService.isEmailAvailable(memberEmail);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", available);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원가입 처리
     * @param member 회원가입 정보
     * @return 회원가입 성공 후 리다이렉트 URL
     */
    @PostMapping("/signup")
    public String signup(Member member) {
        userAuthService.saveMember(member);
        return "redirect:/auth/signupSuccess";
    }

    /**
     * 회원가입 성공 페이지 반환
     * @return 회원가입 성공 페이지 뷰 이름
     */
    @GetMapping("/signupSuccess")
    public String signupSuccess() {
        return "client/auth/signupSuccess";
    }
    
    /**
     * 로그인 페이지 반환
     * @param 카카오 로그인 링크 정보
     * @return 로그인 페이지 뷰 이름
     */
    @GetMapping("/login")
    public String userLoginForm() {
//	public String userLoginForm(Model model) {
//    	String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+clientId+"&redirect_uri="+redirectUri;
//    	model.addAttribute("location", location);
        return "client/auth/userLoginForm";
    }

    /**
     * 로그인 처리
     * @param loginRequest 로그인 정보
     * @param session HTTP 세션 객체
     * @return 로그인 결과 메시지
     */
    @PostMapping("/userLogin")
    @ResponseBody
    public Map<String, String> userLogin(@RequestBody Member loginRequest, HttpSession session) {
        Member member = userAuthService.userLogin(loginRequest.getMemberId(), loginRequest.getMemberPassword());
        Map<String, String> response = new HashMap<>();
        if (member != null) {
            session.setAttribute("loggedInUser", member.getMemberId());
            response.put("message", "Login successful");
        } else {
            response.put("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        return response;
    }
    /*
    @GetMapping("/auth/kakao/callback")
    public RedirectView kakaoCallback(@RequestParam String code, HttpSession session) {
        // 인증 코드로 액세스 토큰을 얻음
        String accessToken = userAuthService.getKakaoAccessToken(code);

        // 액세스 토큰으로 사용자 정보를 얻음
        KakaoUser kakaoUser = userAuthService.getKakaoUserInfo(accessToken);

        // 사용자 정보를 통해 회원 가입 처리
        boolean isRegistered = userAuthService.registerKakaoUser(kakaoUser);
        if (isRegistered) {
            session.setAttribute("loggedInUser", kakaoUser.getId());
            return new RedirectView("/"); // 로그인 후 메인 페이지로 리디렉션
        } else {
            return new RedirectView("/auth/signupFailure"); // 회원 가입 실패 페이지로 리디렉션
        }
    }
*/
    /**
     * 로그인 상태 확인
     * @param session HTTP 세션 객체
     * @return 로그인 상태
     */
    @GetMapping("/isLoggedIn")
    @ResponseBody
    public ResponseEntity<Boolean> isLoggedIn(HttpSession session) {
        String loggedInUserId = (String) session.getAttribute("loggedInUser");
        boolean loggedIn = loggedInUserId != null;
        return ResponseEntity.ok(loggedIn);
    }

    /**
     * 로그인한 사용자 정보 반환
     * @param session HTTP 세션 객체
     * @return 사용자 정보 또는 UNAUTHORIZED 상태
     */
    @GetMapping("/getUserInfo")
    @ResponseBody
    public ResponseEntity<Member> getUserInfo(HttpSession session) {
        String loggedInUserId = (String) session.getAttribute("loggedInUser");
        if (loggedInUserId != null) {
            Member member = userAuthService.getMemberById(loggedInUserId);
            return ResponseEntity.ok(member);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    /**
     * 로그아웃 처리
     * @param session HTTP 세션 객체
     * @return 로그인 페이지로 리다이렉트
     */
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }

    /**
     * 이메일로 인증 코드 발송
     * @param request 이메일을 포함한 요청 본문
     * @return 인증 코드 발송 결과 메시지
     */
    @PostMapping("/sendVerificationCode")
    @ResponseBody
    public ResponseEntity<Map<String, String>> sendVerificationCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Map<String, String> response = new HashMap<>();
        try {
            userAuthService.sendVerificationCode(email);
            response.put("message", "Verification code sent to email");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "인증번호 전송에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 인증 코드 검증
     * @param request 이메일과 인증 코드를 포함한 요청 본문
     * @return 인증 코드 검증 결과 메시지
     */
    @PostMapping("/verifyCode")
    @ResponseBody
    public ResponseEntity<Map<String, String>> verifyCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        boolean valid = userAuthService.verifyCode(email, code);
        Map<String, String> response = new HashMap<>();
        if (valid) {
            response.put("message", "Verification code valid");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "인증번호 검증에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * 사용자 아이디/비밀번호 찾기 폼 반환
     * @return 아이디/비밀번호 찾기 페이지 뷰 이름
     */
    @GetMapping("/findIdPwdForm")
    public String findIdPwdForm() {
        return "client/auth/findIdPwdForm";
    }

    /**
     * 아이디 찾기
     * @param request 이름과 이메일을 포함한 요청 본문
     * @param session HTTP 세션 객체
     * @return 아이디 찾기 결과 메시지
     */
    @PostMapping("/findId")
    @ResponseBody
    public Map<String, String> findId(@RequestBody Map<String, String> request, HttpSession session) {
        String name = request.get("name");
        String email = request.get("email");
        
        String memberId = userAuthService.findIdByNameAndEmail(name, email);
        Map<String, String> response = new HashMap<>();
        
        if (memberId != null) {
            session.setAttribute("memberId", memberId);
            response.put("status", "success");
        } else {
            response.put("status", "error");
            response.put("message", "사용자를 찾을수 없습니다.");
        }
        
        return response;
    }

    /**
     * 아이디 찾기 결과 페이지 반환
     * @param session HTTP 세션 객체
     * @param model 모델 객체
     * @return 아이디 찾기 결과 페이지 뷰 이름
     */
    @GetMapping("/findIdResult")
    public String findIdResult(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");
        if (memberId != null) {
            model.addAttribute("memberId", memberId);
            session.removeAttribute("memberId");
            return "client/auth/findIdResult";
        } else {
            return "redirect:/auth/findIdPwdForm";
        }
    }

    /**
     * 임시 비밀번호 설정
     * @param request 아이디와 이메일을 포함한 요청 본문
     * @return 임시 비밀번호 발송 결과 메시지
     */
    @PostMapping("/resetPassword")
    @ResponseBody
    public Map<String, String> resetPassword(@RequestBody Map<String, String> request) {
        String memberId = request.get("memberId");
        String email = request.get("email");

        Map<String, String> response = new HashMap<>();
        boolean result = userAuthService.resetPassword(memberId, email);

        if (result) {
            response.put("message", "Temporary password has been sent to the provided email.");
        } else {
            response.put("message", "임시 비밀번호 발급에 실패했습니다. 입력값을 다시 확인해주세요.");
        }
        return response;
    }
    
    /**
     * 임시 비밀번호 결과 확인 페이지 반환
     * @return 임시 비밀번호 결과 페이지 뷰 이름
     */
    @GetMapping("/findPwdResult")
    public String findPwdResult() {
        return "client/auth/findPwdResult";
    }
}
