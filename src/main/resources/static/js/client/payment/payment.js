function formatNumberWithCommas(number) {
	return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

var memberName = $("#memberName").data('member-name');
var memberPhone = $("#memberPhone").data('member-phone');

$(document).ready(function() {
	var resRequest = $("#reservationRequest").data('res-request');
	
	$("#reservationRequest").val(resRequest);
	// 전화번호 입력 시 하이픈 추가 기능
	function addHyphenToPhoneNumber(phoneNumberInput) {
		const phoneNumber = phoneNumberInput.value;
		const length = phoneNumber.length;

		if(length >= 9) {
			let numbers = phoneNumber.replace(/[^0-9]/g, "")
				.replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, `$1-$2-$3`);
			phoneNumberInput.value = numbers;
		}
	}

	// 전화번호 입력 필드에 이벤트 리스너 추가
	const phoneInput = document.getElementById("reserverPhone");  // ID 수정
	phoneInput.addEventListener("input", () => {
		addHyphenToPhoneNumber(phoneInput);
	});
	
	
	var resRequest = $("#reservationRequest").data('res-request');
	
	var resNo = $("#reservationInfo").data("resno");
	// 금액 요소를 선택해서 쉼표를 추가
	var originalAmount = $('#payment-amount').text(); // 금액 텍스트 가져오기
	var formattedAmount = formatNumberWithCommas(originalAmount); // 쉼표 추가
	$('#payment-amount').text(formattedAmount); // 쉼표 추가된 금액으로 변경


	$('#autoFillCheckbox').on('change', function() {


		
		console.log(resRequest);
		if ($(this).is(':checked')) {
			// 체크박스가 체크되면 회원의 이름과 전화번호를 자동으로 입력
			$('#reserverName').val(memberName);
			$('#reserverPhone').val(memberPhone);
		} else {
			// 체크박스가 해제되면 입력 필드를 비움
			$('#reserverName').val('');
			$('#reserverPhone').val('');
		}
	});

	//모두 동의 체크박스
	$('#agreeAllCheckbox').on('change', function() {
		var isChecked = $(this).is(':checked');
		// 모두 동의 체크박스를 선택/해제하면 두 체크박스의 상태를 동일하게 변경
		$('#termsAgree').prop('checked', isChecked);
		$('#privacyAgree').prop('checked', isChecked);
		
	});

	$('.payment-button').click(async function() {
		var reserverName = $("#reserverName").val().trim();
		var reserverPhone = $("#reserverPhone").val().trim();
		var reserverRequest = $("#reservationRequest").val();
	console.log(reserverName);
		if (reserverName === "") {
			alert("예약자명을 입력하세요.");
			return; // 예약자명이 비어 있으면 결제 진행 막음
		}

		if (reserverPhone === "") {
			alert("전화번호를 입력하세요.");
			return; // 전화번호가 비어 있으면 결제 진행 막음
		}
		// 약관 동의 체크 여부 확인
		if (!$("#termsAgree").is(":checked")) {
			alert("이용 약관에 동의해 주세요.");
			return; // 약관 동의가 안 된 경우 결제 진행 막음
		}

		if (!$("#privacyAgree").is(":checked")) {
			alert("개인정보 처리방침에 동의해 주세요.");
			return; // 개인정보 동의가 안 된 경우 결제 진행 막음
		}


		var paymentMethod = $(this).data('method');
		const spName = $(".spName").text();

		console.log("Selected payment method: " + paymentMethod);

		// 기본값 설정
		let channelKey = "";
		let payMethod = "";

		// 결제 방법에 따라 키 값 설정
		if (paymentMethod === "kakao") {
			channelKey = "channel-key-1972b134-d930-4897-9599-f805035dac66";
			payMethod = "EASY_PAY";
		} else if (paymentMethod === "toss") {
			channelKey = "channel-key-31017ccc-371d-440c-b9e4-459bc3323d14"; // 실제 토스 채널 키로 변경
			payMethod = "EASY_PAY";
		} else if (paymentMethod === "card") {
			channelKey = "channel-key-f20734f6-5df3-406f-8d66-56a0ffa0aa1f"; // 실제 일반 결제 채널 키로 변경
			payMethod = "CARD";
		} else if (paymentMethod === "mobile") {
			channelKey = "channel-key-f20734f6-5df3-406f-8d66-56a0ffa0aa1f"; // 실제 일반 결제 채널 키로 변경
			payMethod = "MOBILE";
		}

		try {
			const paymentId = `pay-${crypto.randomUUID().substring(0, 20)}`; // 주문 번호를 40자 이하로 제한

			const requestOptions = {
				storeId: "store-6fcc723b-ff52-4915-9fdd-d5bc06e8c273",
				channelKey: channelKey,
				paymentId: paymentId,
				orderName: spName,
				totalAmount: originalAmount,
				currency: "KRW",
				payMethod: payMethod,

			};

			// 결제 방법이 모바일일 경우 추가 옵션 설정
			if (paymentMethod === "mobile") {
				requestOptions.bypass = {
					"kcp_v2": {
						"site_logo": "https://portone.io/assets/portone.jpg",
						"skin_indx": 6,
						"shop_user_id": "user_id1",
						"site_name": "모바일 결제"
					}
				};
			}

			// 결제 요청 함수 호출
			const response = await PortOne.requestPayment(requestOptions);

			if (response.code != null) {
				// 오류 발생 시 처리
				return alert(response.message);
			}

			const tid = response.tid;

			// 결제가 성공하면 서버로 결제 완료 정보 전송
			const SERVER_BASE_URL = "http://localhost:8080";
			const notified = await fetch(`${SERVER_BASE_URL}/payment/paymentCheck`, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({
					paymentId: paymentId,
					tid: tid,
					orderName: requestOptions.orderName,
					totalAmount: requestOptions.totalAmount,
					payMethod: payMethod,
					resNo: Number(resNo),
					resName: reserverName,
					resPhone: reserverPhone,
					resRequest: reserverRequest,
				}),
			});

			if (notified.ok) {
				const paymentNo = await notified.text();
				// 결제 성공 시 페이지 이동
				window.location.replace("/payment/complete/" + paymentNo);
			} else {

				alert(await notified.text());
			}

		} catch (error) {
			console.error("결제 요청 중 오류가 발생했습니다:", error);
			alert("결제 요청 중 오류가 발생했습니다.");
		}
	});


});
