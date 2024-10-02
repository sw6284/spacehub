$(document).ready(function() {
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

    // 페이지 로드 시 URL에서 상태를 읽어와서 필터 드롭다운에 설정
    var currentState = new URLSearchParams(window.location.search).get('state');
    if (currentState) {
        $('#status-filter').val(currentState);
    }

    $('#status-filter').change(function() {
        var selectedState = $(this).val();
        $('#hiddenState').val(selectedState);
        $('#searchForm').submit();  // 폼 제출로 서버에 필터 값 전달
    });

    $(".goDetail").click(function() {
        let memberNo = $(this).closest("tr").data("memberNo");
        let selectedState = $('#status-filter').val();
        let rowStatus = $(this).closest("tr").data("status");
        if (selectedState === 'all' || rowStatus === selectedState) {
            window.location.href = "/admin/memberManage/" + memberNo;
        } else {
            alert("현재 상태 필터와 맞지 않는 항목입니다.");
        }
    });

    // 아이디와 이메일 마스킹 처리
    $("table tbody tr").each(function() {
        let id = $(this).data("memberId");
        let email = $(this).data("memberEmail");

        // 마스킹 처리
        $(this).find(".memberId").text(maskText(id, 'id'));
        $(this).find(".memberEmail").text(maskText(email, 'email'));
    });
});
