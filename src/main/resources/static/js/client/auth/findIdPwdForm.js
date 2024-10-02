$(document).ready(function() {
    function showTab(tabId, event) {
        $('.tab-content').hide();
        $('.tab-button').removeClass('active');
        $('#' + tabId).show();
        $(event.currentTarget).addClass('active');
    }

    // 기본적으로 첫 번째 탭을 표시
    var defaultTabId = $('.tab-button.active').data('tab-id');
    if (defaultTabId) {
        $('#' + defaultTabId).show(); // 기본적으로 활성화된 탭 콘텐츠 표시
    } else {
        // 기본 탭이 설정되지 않은 경우, 첫 번째 탭을 기본 탭으로 설정
        var firstTabId = $('.tab-button').first().data('tab-id');
        if (firstTabId) {
            $('#' + firstTabId).show();
            $('.tab-button').first().addClass('active');
        }
    }

    // 탭 버튼 클릭 시
    $('.tab-button').click(function(event) {
        var tabId = $(this).data('tab-id');
        showTab(tabId, event);
    });

    // 아이디 찾기 버튼 클릭 시
    $("#findIdBtn").on("click", function(event) {
        event.preventDefault(); // 기본 폼 제출 방지

        // 유효성 검사
        if (!chkData("#name-id", "이름을")) return;
        if (!chkData("#email-id", "이메일을")) return;

        // 폼 데이터 수집
        const formData = {
            name: $("#name-id").val(),
            email: $("#email-id").val()
        };

        // AJAX 요청
        $.ajax({
            url: "/auth/findId",
            type: "POST",
            contentType: "application/json; charset=UTF-8", // JSON 형식으로 설정
            data: JSON.stringify(formData), // 데이터 JSON 문자열로 변환
            success: function(response) {
                if (response.status === "success") {
                    // actionProcess를 사용하여 결과 페이지로 이동
                    actionProcess("#findIdForm", "GET", "/auth/findIdResult");
                } else {
                    alert(response.message);
                }
            },
            error: function() {
                alert("서버 오류 발생. 다시 시도해 주세요.");
            }
        });
    });

	$("#findPwdBtn").on("click", function(event) {
	    event.preventDefault(); // 기본 폼 제출 방지

	    // 데이터 유효성 검사
	    if (!chkData("#name-pwd", "이름을")) return;
	    if (!chkData("#id-pwd", "아이디를")) return;
	    if (!chkData("#email-pwd", "이메일을")) return;

	    // 폼 데이터 추출
	    var formData = {
	        name: $("#name-pwd").val(),
	        memberId: $("#id-pwd").val(), // server expects this parameter
	        email: $("#email-pwd").val()
	    };

	    // AJAX 요청 보내기
	    $.ajax({
	        url: "/auth/resetPassword",
	        type: "POST",
	        contentType: "application/json; charset=UTF-8", // JSON 형식으로 설정
	        data: JSON.stringify(formData), // 데이터 JSON 문자열로 변환
	        success: function(response) {
	            if (response.message.includes("전송되었습니다")) {
	                // actionProcess를 사용하여 결과 페이지로 이동
	                locationProcess("/auth/findPwdResult");
	            } else {
	                alert(response.message); // 한글 메시지 표시
	            }
	        },
	        error: function(xhr) {
	            console.error(xhr.responseText); // 상세 오류 로그 출력
	            alert("비밀번호 재설정에 실패했습니다. 다시 시도해 주세요."); // 한글 메시지 표시
	        }
	    });
	});

});
