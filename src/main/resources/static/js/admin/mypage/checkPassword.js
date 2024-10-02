$(document).ready(function() {
    $('#checkPwdBtn').on('click', function() {
        var password = $('#password').val();

        // 비밀번호 유효성 체크
        if (!chkData("#password", "비밀번호를")) return;

        // 비밀번호 확인 AJAX 요청
        $.ajax({
            type: 'POST',
            url: '/admin/myPage/confirmPassword', // URL 확인
            data: { password: password }, // 전송할 데이터
            success: function(response) {
                if (response === 'OK') {
                    locationProcess("/admin/myPage/updateForm");
                } else {
                    alert('비밀번호가 올바르지 않습니다.');
                }
            },
            error: function(xhr, status, error) {
                // 오류 처리
                alert('비밀번호 확인 중 오류가 발생했습니다.');
                console.error('Status:', status);
                console.error('Error:', error);
                console.error('Response Text:', xhr.responseText);
            }
        });
    });
});
