$(document).ready(function() {
    $('#deleteAccountButton').on('click', function() {
        var password = $('#password').val();

		if (!chkData("#password", "비밀번호를")) return; 

        // 비밀번호 확인 AJAX 요청
        $.ajax({
            type: 'POST',
            url: '/myPage/confirmPassword',
            data: { password: password },
            success: function(response) {
                if (response === 'OK') {
                    if (confirm('정말로 계정을 삭제하시겠습니까?')) {
                        $.ajax({
                            type: 'POST',
                            url: '/myPage/deleteMember',
                            success: function() {
                                // 삭제 성공 시, 로그인 페이지로 리다이렉트
                                window.location.href = '/auth/login';
                            },
                            error: function() {
                                // 오류 처리
                                alert('계정 삭제 중 오류가 발생했습니다.');
                            }
                        });
                    }
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
