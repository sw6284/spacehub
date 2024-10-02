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
    let memberId = $("#memberId").text();
    let memberEmail = $("#memberEmail").text();
    let memberPhone = $("#memberPhone").text();

    // 마스킹 처리
    $("#memberId").text(maskText(memberId, 'id'));
    $("#memberEmail").text(maskText(memberEmail, 'email'));
    $("#memberPhone").text(maskText(memberPhone, 'phone'));

    $("#memberListBtn").on("click", function(){
        locationProcess("/admin/memberManage");
    });

    $("#memberDeleteBtn").on("click", function(){
        // Confirm dialog
        if (confirm("정말 삭제하시겠습니까?")) {
            // AJAX request to delete the member
            $.ajax({
                url: '/admin/memberManage/deleteMember',  // Actual URL for deletion
                type: 'POST',
                data: { memberId: memberId }, 
                success: function(response) {
                    // On successful deletion
                    alert("회원이 삭제되었습니다.");
                    locationProcess("/admin/memberManage");
                },
                error: function(xhr, status, error) {
                    // On failure
                    console.error("Status:", xhr.status);
                    console.error("Response Text:", xhr.responseText);
                    console.error("Error:", error);
                    alert("회원 삭제 중 오류가 발생했습니다.");
                }
            });
        }
    });
});
