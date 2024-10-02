
$(".cancelBtn").on("click", function() {

	// 클릭된 버튼의 가장 가까운 <tr>에서 data-resno 값을 가져옴
	var resNo = $(this).closest("tr").data("resno");
	console.log(resNo);

	$.ajax({
		url: '/payment/payCancel',  // 요청을 보낼 URL
		type: 'POST',            // HTTP 메서드 (POST)
		contentType: 'application/json', // 전송할 데이터의 타입
		data: JSON.stringify({
			resNo: resNo,
		}),
		success: function(response) {
			// 요청이 성공했을 때 실행되는 함수
			console.log('결제 취소 성공:', response);
			alert('결제가 성공적으로 취소되었습니다.');

			location.reload();
		},
		error: function(xhr, status, error) {
			// 요청이 실패했을 때 실행되는 함수
			console.error('결제 취소 실패:', error);
			alert('결제 취소에 실패했습니다.');
		}
	});



})






$("#searchType").on("change", function() {
	if ($("#searchType").val() != "date") {
		$("#dateArea").hide();
		$("#textArea").show();
		$("#searchKeyword").val("");
		$("#searchKeyword").focus();
	}
	if ($("#searchType").val() == "date") {
		$("#textArea").hide();
		$("#dateArea").show();
	}
});



$("#searchButton").on("click", function() {
    var searchType = $("#searchType").val();  // 검색 타입
    var searchKeyword = $("#searchKeyword").val();  // 검색어
    var searchDate = $("#dateArea").val();  // 예약 날짜

    // AJAX 요청으로 검색 실행
    $.ajax({
        url: '/admin/reservationManagerSearch',  // 요청할 URL
        type: 'GET',  // HTTP GET 메서드
        data: {
            searchType: searchType,
            keyword: searchKeyword,
            date: searchDate
        },
        success: function(response) {
           
            $("#reservation-list").html(response);
        },
        error: function(error) {
            console.error("검색 오류:", error);
        }
    });
});