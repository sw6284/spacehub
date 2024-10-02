let isIdVerified = false; // 아이디 인증 상태 변수
let isEmailVerified = false; // 이메일 인증 상태 변수

// 아이디 중복 확인 버튼 클릭 시
$("#idCheckBtn").on("click", function () {
    const memberId = $("#memberId").val();

    if (!chkData("#memberId", "아이디를")) return; // 아이디 입력 여부 확인

    $.ajax({
        url: '/auth/checkId',
        type: 'POST',
        data: JSON.stringify({ memberId: memberId }), // JSON 형식으로 변환
        contentType: 'application/json', // 요청 헤더 설정
        dataType: 'json', // 응답 데이터 형식 설정
        success: function(response) {
            if (response.available) {
                alert("사용 가능한 아이디입니다.");
                isIdVerified = true; // 아이디 인증 완료
            } else {
                alert("이미 사용 중인 아이디입니다.");
                isIdVerified = false; // 아이디 인증 실패
            }
        },
        error: function(xhr, status, error) {
            console.error("Status:", xhr.status); // 상태 코드
            console.error("Response Text:", xhr.responseText); // 응답 텍스트
            console.error("Error:", error); // 오류
            alert("아이디 확인 중 오류가 발생했습니다.");
        }
    });
});

// 이메일 인증 요청 버튼 클릭 시
$("#verifyBtn").on("click", function () {
    const memberEmail = $("#memberEmail").val();

    if (!chkData("#memberEmail", "이메일을")) return; // 이메일 입력 여부 확인

    $.ajax({
        url: '/auth/sendVerificationCode',
        type: 'POST',
        data: JSON.stringify({ email: memberEmail }), // JSON 형식으로 변환
        contentType: 'application/json', // 요청 헤더 설정
        dataType: 'json', // 응답 데이터 형식 설정
        success: function(response) {
            if (response.message === "Verification code sent to email") {
                alert("인증번호를 이메일로 발송했습니다.");
                isEmailVerified = false; // 초기 상태로 설정
            } else {
                alert("인증번호 발송에 실패했습니다.");
            }
        },
        error: function(xhr, status, error) {
            console.error("Status:", xhr.status);
            console.error("Response Text:", xhr.responseText);
            console.error("Error:", error);
            alert("인증번호 발송 중 오류가 발생했습니다.");
        }
    });
});

// 인증번호 확인 버튼 클릭 시
$("#verifyCheckBtn").on("click", function () {
    const verificationCode = $("#verificationCode").val().trim(); // 공백 제거
    const memberEmail = $("#memberEmail").val();

    if (!chkData("#verificationCode", "인증번호를")) return; // 인증번호 입력 여부 확인
    if (!chkData("#memberEmail", "이메일을")) return; // 이메일 입력 여부 확인

    $.ajax({
        url: '/auth/verifyCode',
        type: 'POST',
        data: JSON.stringify({ code: verificationCode, email: memberEmail }), // JSON 형식으로 변환
        contentType: 'application/json', // 요청 헤더 설정
        dataType: 'json', // 응답 데이터 형식 설정
        success: function(response) {
            if (response.message === "Verification code valid") {
                alert("이메일 인증이 완료되었습니다.");
                isEmailVerified = true; // 이메일 인증 완료
            } else {
                alert("인증번호가 유효하지 않거나 만료되었습니다.");
                isEmailVerified = false; // 인증 실패
            }
        },
        error: function(xhr, status, error) {
            console.error("Status:", xhr.status);
            console.error("Response Text:", xhr.responseText);
            console.error("Error:", error);
            alert("인증번호 확인 중 오류가 발생했습니다.");
        }
    });
});

// 회원가입 버튼 클릭 시
$("#signupBtn").on("click", function () {
    // 필수 입력 필드 확인
    if (!chkData("#memberId", "아이디를")) return;
    if (!chkData("#memberPassword", "비밀번호를")) return;
    if (!chkData("#memberPasswordConfirm", "비밀번호 확인을")) return;
    if (!chkData("#memberEmail", "이메일을")) return;
    if (!chkData("#memberPhone", "전화번호를")) return;

    // 비밀번호 일치 확인
    if ($("#memberPassword").val() !== $("#memberPasswordConfirm").val()) {
        alert("비밀번호가 일치하지 않습니다.");
        return;
    }

    // 비밀번호 유효성 검사
    const pwdRegex = /^(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,12}$/;
    if (!pwdRegex.test($("#memberPassword").val())) {
        alert("비밀번호는 영소문자, 숫자, 특수문자를 포함하여 8~12자이어야 합니다.");
        return;
    }

    // 아이디 인증이 완료되었는지 확인
    if (!isIdVerified) {
        alert("아이디 인증을 진행해 주세요.");
        return;
    }

    // 이메일 인증이 완료되었는지 확인
    if (!isEmailVerified) {
        alert("이메일 인증을 진행해 주세요.");
        return;
    }

    // 모든 조건이 만족되면 회원가입 폼 제출
    actionProcess("#signupForm", "post", "/auth/signup");
});
