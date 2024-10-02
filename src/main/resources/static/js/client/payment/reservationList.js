let lastOpenedDiv = null;  // 마지막으로 열렸던 details div를 저장하는 변수

$(document).ready(function() {
	$(".dbtn-primary").on("click", function() {
		var form = $(this).siblings('form');
		var resNo = form.find('input[type="hidden"]').val();

		$.ajax({
			type: "POST",
			url: "/reservation/orders",
			contentType: "application/json; charset=UTF-8",
			data: JSON.stringify({
			            resNo: resNo  
			        }),
			success: function(response) {
				// 서버가 반환한 URL로 페이지 리다이렉트
				console.log("Redirect URL:", response);  // URL 확인용 로그
				window.location.href = response;  // 성공 시 받은 URL로 리다이렉트
			},
			error: function(error) {
				console.log("Error occurred: ", error);
			}
		});
	});




	$('#periodSelect').on('change', function() {
		var selectedPeriod = $(this).val();  // 선택된 기간 값
		console.log(selectedPeriod);
		$.ajax({
			type: 'GET',
			url: '/reservation/filter',  // 컨트롤러의 필터링 URL
			data: { period: selectedPeriod },  // 선택된 기간을 서버로 전달
			success: function(response) {
				// 성공적으로 데이터를 받아오면 #reservation-list 부분을 업데이트
				$('#reservation-list').html(response);
			},
			error: function(xhr, status, error) {
				console.error("Error occurred while filtering reservations:", error);
			}
		});
	});


	$(".clickable").on("click", function() {
		// 클릭된 div의 data-no 값 가져오기
		const detailNo = $(this).data('no');  // 예: 'btn-15'

		// 'btn-' 부분을 'detail-'로 바꾸기
		const detailId = 'detail-' + detailNo.split('btn-')[1];

		// 해당하는 details div를 선택 (예: 'detail-15')
		const detailDiv = $(`[data-no="${detailId}"]`);

		// 이전에 열려 있던 details div를 닫기 (애니메이션 포함)
		if (lastOpenedDiv && lastOpenedDiv[0] !== detailDiv[0]) {
			lastOpenedDiv.slideUp(300);  // 300ms 동안 서서히 접힘
		}

		// 현재 클릭된 details div를 애니메이션으로 토글
		if (detailDiv) {
			detailDiv.slideToggle(300);  // 300ms 동안 서서히 펼쳐지거나 접힘
			lastOpenedDiv = detailDiv;  // 현재 열린 details div를 기록
		}

	});
	
	
	$(".btn-danger").on("click", function() {
	    // 동적 값 추출
	    const rno = $(this).closest(".list").find(".clickable").data('no').split('-')[1];  // 예약 번호
	    const rnm = $(this).closest(".list").find(".resName").text();  // 예약자 이름
		const rst = $(this).closest(".list").find(".startTime").text().trim();  // 퇴실 시간
	    const ret = $(this).closest(".list").find(".endTime").text().trim();  // 퇴실 시간

	    // 팝업창 URL에 파라미터로 필요한 데이터를 전달
	    const popupURL = `/payment/cancelPage?rno=${encodeURIComponent(rno)}&rst=${encodeURIComponent(rst)}&ret=${encodeURIComponent(rnm)}&rnm=${encodeURIComponent(ret)}`;
	    const popupProperties = "width=600,height=400,scrollbars=yes";

	    // 팝업 창 열기
	    window.open(popupURL, "Popup", popupProperties);
	});

});