$(document).ready(function() {
    // 로그인 페이지가 로드될 때 로그인 상태를 확인합니다.
    $.ajax({
        type: "GET",
        url: "/auth/isLoggedIn",
        success: function(response) {
            if (response) {
                // 로그인 상태일 경우 알림을 띄우고 메인 페이지로 리디렉션
                alert("이미 로그인한 상태입니다.");
				
                // 알림창이 닫힌 후에 메인 페이지로 리디렉션
                setTimeout(function() {
                    window.location.href = "/"; // 메인 페이지로 리디렉션
                }, 100); // 100ms 후에 리디렉션
            }
        },
        error: function(xhr, status, error) {
            console.error("로그인 상태 확인 실패:", status, error);
        }
    });
});


$("#signupBtn").on("click", function(){
    locationProcess("/auth/signupForm");
});

$("#findIdBtn").on("click", function(){
    locationProcess("/auth/findIdPwdForm");
});

$("#login-btn").on("click", function(){
    // 유효성 검사
    if(!chkData("#userId", "아이디를")) return;
    if(!chkData("#userPasswd", "비밀번호를")) return;
    
    // 로그인 데이터 준비
    let loginData = {
        memberId: $("#userId").val(),      // 서버에서 기대하는 필드명으로 수정
        memberPassword: $("#userPasswd").val()  // 서버에서 기대하는 필드명으로 수정
    };

    // AJAX 요청
    $.ajax({
        type: "POST",
        url: "/auth/userLogin", // 서버의 로그인 URL
        data: JSON.stringify(loginData),
        contentType: "application/json; charset=utf-8",
        dataType: "json" // 서버에서 JSON 응답을 받기 위해 dataType을 설정합니다.
    }).done(function(response) {
        if (response.message === "Login successful") {
            alert("로그인 성공!");
            // 로그인 후 이동할 페이지로 변경하세요.
            window.location.href = "/"; // 로그인 후 이동할 페이지로 변경하세요.
        } else {
            alert(response.message);
        }
    }).fail(function(xhr, status, error) {
        console.error("로그인 요청 실패:", status, error);
        alert("로그인 요청에 실패했습니다. 잠시 후 다시 시도해 주세요.");
    });
});


// Kakao 로그인 버튼 처리
$("#kakao-btn").on("click", function() {
	/*var kakaoLoginUrl = $(this).data("kakao-url");
    window.location.href = kakaoLoginUrl;*/
});
