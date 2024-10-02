let isEmailVerified = false; // 이메일 인증 상태 변수
let originalName = $("#memberName").val(); // 원래 이름
let originalPhone = $("#memberPhone").val(); // 원래 전화번호
let originalEmail = $("#memberEmail").val(); // 원래 이메일

$(document).ready(function() {
    // 비밀번호와 비밀번호 확인 일치 여부 확인
    $("#newPassword, #newPasswordConfirm").on("keyup", function() {
        let password = $("#newPassword").val();
        let passwordConfirm = $("#newPasswordConfirm").val();

        if (password && password !== passwordConfirm) {
            $("#newPasswordConfirm").css("border", "1px solid red");
        } else {
            $("#newPasswordConfirm").css("border", "1px solid #ccc");
        }
    });

	// 이메일 인증 요청 버튼 클릭 시
	$("#verifyBtn").on("click", function() {
	    const memberEmail = $("#memberEmail").val();

	    // 이메일 입력 여부 확인
	    if (!chkData("#memberEmail", "이메일을")) {
	        $("#memberEmail").focus(); // 이메일 입력 필드에 포커스
	        return;
	    }

	    // 이메일이 변경되었는지 확인
	    if (memberEmail === originalEmail) {
	        alert("이메일이 변경되지 않았습니다.");
	        return;
	    }

	    // 이메일 중복 체크
	    $.ajax({
	        url: '/auth/checkEmail',
	        type: 'POST',
	        data: JSON.stringify({ memberEmail: memberEmail }), // JSON 형식으로 변환
	        contentType: 'application/json', // 요청 헤더 설정
	        dataType: 'json', // 응답 데이터 형식 설정
	        success: function(response) {
	            if (response.available) {
	                // 이메일 중복이 없는 경우, 인증번호 발송 요청
	                $.ajax({
	                    url: '/auth/sendVerificationCode',
	                    type: 'POST',
	                    data: JSON.stringify({ email: memberEmail }), // JSON 형식으로 변환
	                    contentType: 'application/json', // 요청 헤더 설정
	                    dataType: 'json', // 응답 데이터 형식 설정
	                    success: function(response) {
	                        if (response.message === "Verification code sent to email") {
	                            alert("인증번호를 이메일로 발송했습니다.");
	                            isEmailVerified = false; // 이메일 인증 상태를 초기화
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
	            } else {
	                alert("이미 사용 중인 이메일입니다.");
	                $("#memberEmail").focus(); // 이메일 입력 필드에 포커스
	            }
	        },
	        error: function(xhr, status, error) {
	            console.error("Status:", xhr.status);
	            console.error("Response Text:", xhr.responseText);
	            console.error("Error:", error);
	            alert("이메일 중복 확인 중 오류가 발생했습니다.");
	        }
	    });
	});

    // 인증번호 확인 버튼 클릭 시
    $("#verifyCheckBtn").on("click", function() {
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

    $("#userUpdateBtn").on("click", function(event) {
        event.preventDefault(); // 폼의 기본 제출 동작을 막음

        // 이메일이 변경되었고 인증이 완료되었는지 확인
        const memberEmail = $("#memberEmail").val();
        if (memberEmail !== originalEmail && !isEmailVerified) {
            alert("이메일 인증이 완료되지 않았습니다.");
            return;
        }

        // 폼 데이터 수집
        let formData = {
            memberPassword: $("#newPassword").val(),
            memberName: $("#memberName").val(),
            memberPhone: $("#memberPhone").val(),
            memberEmail: $("#memberEmail").val() // 이메일 추가
        };

        console.log("폼 데이터:", formData); // 데이터가 올바르게 수집되었는지 확인

        $.ajax({
            url: '/myPage/updateMember',
            type: 'POST',
            contentType: 'application/json', // 데이터 형식 설정
            data: JSON.stringify(formData), // JSON 형식으로 전송
            success: function(response) {
                console.log("서버 응답:", response); // 서버 응답 확인
                if (response === "UPDATED") {
                    alert("회원 정보가 성공적으로 업데이트되었습니다.");
                    window.location.href = '/myPage'; // 성공 후 페이지 이동
                } else {
                    alert("회원 정보 업데이트에 실패했습니다.");
                }
            },
            error: function(xhr) {
                console.error("Error:", xhr.status, xhr.statusText);
                alert("회원 정보 업데이트 중 오류가 발생했습니다.");
            }
        });
    });
	
	
	$("#resetBtn").on("click", function() {
	       // 입력 필드와 상태 초기화
	       $("#newPassword").val('');
	       $("#newPasswordConfirm").val('');
	       $("#memberName").val(originalName);
	       $("#memberPhone").val(originalPhone);
	       $("#memberEmail").val(originalEmail);
	       $("#verificationCode").val('');
	       
	       $("#newPasswordConfirm").css("border", "1px solid #ccc"); // 비밀번호 확인 필드의 테두리 초기화

	       isEmailVerified = false;
	}); 
	
	$("#cancelBtn").on("click", function(){
		locationProcess("/myPage");
	});
});


