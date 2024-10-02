let originalName = $("#admName").val(); // 원래 이름
let originalPhone = $("#admPhone").val(); // 원래 전화번호
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

    $("#adminUpdateBtn").on("click", function(event) {
        event.preventDefault(); // 폼의 기본 제출 동작을 막음
        // 폼 데이터 수집
        let formData = {
            admPasswd: $("#newPassword").val(),
            admName: $("#admName").val(),
            admPhone: $("#admPhone").val(),
        };

        console.log("폼 데이터:", formData); // 데이터가 올바르게 수집되었는지 확인

        $.ajax({
            url: '/admin/myPage/updateAdmin',
            type: 'POST',
            contentType: 'application/json', // 데이터 형식 설정
            data: JSON.stringify(formData), // JSON 형식으로 전송
            success: function(response) {
                console.log("서버 응답:", response); // 서버 응답 확인
                if (response === "UPDATED") {
                    alert("회원 정보가 성공적으로 업데이트되었습니다.");
                    window.location.href = '/admin/myPage'; // 성공 후 페이지 이동
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
        $("#admName").val(originalName);
        $("#admPhone").val(originalPhone);
        $("#verificationCode").val('');
        
        $("#newPasswordConfirm").css("border", "1px solid #ccc"); // 비밀번호 확인 필드의 테두리 초기화
		
	}); 
		
	$("#cancelBtn").on("click", function(){
		locationProcess("/admin/myPage");
	});
});
