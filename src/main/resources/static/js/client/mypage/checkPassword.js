$(document).ready(function() {
    $('#checkPwdBtn').on('click', function() {
        var password = $('#password').val();

		if (!chkData("#password", "비밀번호를")) return; 

        // 비밀번호 확인 AJAX 요청
        $.ajax({
            type: 'POST',
            url: '/myPage/confirmPassword',
            data: { password: password },
            success: function(response) {
                if (response === 'OK') {
                    locationProcess("/myPage/updateForm");
                } else {
                    alert('비밀번호가 올바르지 않습니다.');
                }
            },
            error: function() {
                // 오류 처리
                alert('비밀번호 확인 중 오류가 발생했습니다.');
            }
        });
    });
});
