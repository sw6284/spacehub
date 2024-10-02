$(document).ready(function() {
    let isSuperAdmin = false; // 최고 관리자 여부를 저장할 변수

    // 아이디 및 이메일 마스킹 함수
    function maskText(text, type) {
        if (!text) return ''; // 텍스트가 없는 경우 빈 문자열 반환

        let masked = text.slice(0, 3);
        if (type === 'email') {
            let domainIndex = text.indexOf("@");
            let domain = text.slice(domainIndex);
            return masked + "*******" + domain;
        } else {
            return masked + "*********";
        }
    }

    // 관리자 정보를 가져와 최고 관리자 여부를 확인
    $.ajax({
        type: "GET",
        url: "/admin/getAdminInfo",
        success: function(response) {
            if (response && response.admSuper === "Y") {
                isSuperAdmin = true; // 최고 관리자인 경우 true로 설정
                $("#insertAdminBtn").show(); // 최고 권한자일 경우 버튼 표시
            } else {
                $("#insertAdminBtn").hide(); // 최고 권한자가 아닐 경우 버튼 숨기기
            }
        },
        error: function(xhr, status, error) {
            console.error("사용자 정보 요청 실패:", status, error);
            $("#insertAdminBtn").hide();
        }
    });

    // 필터에 따라 테이블 행을 숨기거나 표시
    $('#status-filter').change(function() {
        var selectedState = $(this).val();
        $('#hiddenState').val(selectedState); // 필터 상태를 hidden 필드에 저장
        $('#searchForm').submit(); // 폼 제출로 서버에 필터 값 전달
    });

    // 상세 정보를 볼 때 최고 관리자만 접근 가능하도록
    $(".goDetail").click(function() {
        if (!isSuperAdmin) {
            alert("상세 정보를 볼 수 있는 권한이 없습니다."); // 최고 관리자가 아닐 경우 경고 메시지 표시
            return;
        }
        let admNo = $(this).closest("tr").data("adminNo");
        let selectedStatus = $('#status-filter').val();
        let rowStatus = $(this).closest("tr").data("status");
        if (selectedStatus === 'all' || rowStatus === selectedStatus) {
            locationProcess("/admin/adminManage/" + admNo);
        }
    });

    // 신규 관리자 버튼 클릭 시 처리
    $("#insertAdminBtn").on("click", function() {
        locationProcess("/admin/adminManage/insertAdminForm");
    });

    // 아이디와 이메일 마스킹 처리
    $("table tbody tr").each(function() {
        let id = $(this).data("id");
        let email = $(this).data("email");

        // 마스킹 처리
        $(this).find(".adminId").text(maskText(id, 'id'));
        $(this).find(".adminEmail").text(maskText(email, 'email'));
    });
});
