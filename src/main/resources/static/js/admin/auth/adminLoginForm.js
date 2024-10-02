$("#login-btn").on("click", function(){
    // 유효성 검사
    if(!chkData("#admId", "아이디를")) return;
    if(!chkData("#admPasswd", "비밀번호를")) return;
    
    // 로그인 데이터 준비
    let loginData = {
        admId: $("#admId").val(),      
        admPasswd: $("#admPasswd").val() 
    };

    // AJAX 요청
    $.ajax({
        type: "POST",
        url: "/admin/adminLogin",
        data: JSON.stringify(loginData),
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    }).done(function(response) {
        if (response.message === "Login successful") {
            alert("로그인 성공!");
            window.location.href = "/admin/main";
        } else {
            alert(response.message);
        }
    }).fail(function(xhr, status, error) {
        console.error("로그인 요청 실패:", status, error);
        alert("로그인 요청에 실패했습니다. 잠시 후 다시 시도해 주세요.");
    });
});
