$(document).ready(function() {
    // 아이디, 이메일, 전화번호 마스킹 함수
    function maskText(text, type) {
        if (!text) return ''; // 텍스트가 없는 경우 빈 문자열 반환

        let masked = text.slice(0, 3);
        if (type === 'email') {
            let domainIndex = text.indexOf("@");
            let domain = text.slice(domainIndex);
            return masked + "*******" + domain;
        } else if (type === 'phone') {
            return masked + "-****-" + text.slice(-4); // 전화번호의 마지막 4자리만 보이게
        } else {
            return masked + "*********";
        }
    }

    // 페이지 로드 시 마스킹 처리
    let admId = $("#admId").text();
    let admEmail = $("#admEmail").text();
    let admPhone = $("#admPhone").text();

    // 마스킹 처리
    $("#admId").text(maskText(admId, 'id'));
    $("#admEmail").text(maskText(admEmail, 'email'));
    $("#admPhone").text(maskText(admPhone, 'phone'));

    $("#adminListBtn").on("click", function(){
        locationProcess("/admin/adminManage");
    });

    $("#adminDeleteBtn").on("click", function(){
        // Confirm dialog
        if (confirm("정말 삭제하시겠습니까?")) {
            // AJAX request to delete the admin
            $.ajax({
                url: '/admin/deleteAdmin',
                type: 'POST',
                data: { admId: admId },
                success: function(response) {
                    // On successful deletion
                    alert("관리자가 삭제되었습니다.");
                    locationProcess("/admin/adminManage");
                },
                error: function(xhr, status, error) {
                    // On failure
                    console.error("Status:", xhr.status);
                    console.error("Response Text:", xhr.responseText);
                    console.error("Error:", error);
                    alert("관리자 삭제 중 오류가 발생했습니다.");
                }
            });
        }
    });
});
