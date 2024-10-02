// 회원가입 버튼 클릭 시
$("#signupBtn").on("click", function () {
    // 필수 입력 필드 확인
    if (!chkData("#admId", "아이디를")) {
        $("#admId").focus(); // 아이디 입력 필드에 포커스
        return;
    }
    if (!chkData("#admPasswd", "비밀번호를")) {
        $("#admPasswd").focus(); // 비밀번호 입력 필드에 포커스
        return;
    }
	if (!chkData("#admName", "이름을")) {
        $("#admName").focus(); // 이름 입력 필드에 포커스
        return;
    }
    if (!chkData("#admEmail", "이메일을")) {
        $("#admEmail").focus(); // 이메일 입력 필드에 포커스
        return;
    }
    if (!chkData("#admPhone", "전화번호를")) {
        $("#admPhone").focus(); // 전화번호 입력 필드에 포커스
        return;
    }

    // 비밀번호 유효성 검사
    const pwdRegex = /^(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,12}$/;
    if (!pwdRegex.test($("#admPasswd").val())) {
        alert("비밀번호는 영소문자, 숫자, 특수문자를 포함하여 8~12자이어야 합니다.");
        $("#admPasswd").focus(); // 비밀번호 입력 필드에 포커스
        return;
    }
    actionProcess("#signupForm", "post", "/admin/adminManage/insertAdmin");
});

$("#cancelBtn").on("click", function(){
	locationProcess("/admin/adminManage");
});
