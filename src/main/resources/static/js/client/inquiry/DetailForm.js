$(document).ready(function() {
    $("#inquiryDeleteBtn").on("click", function() {
        if (confirm("정말 삭제하시겠습니까?")) {
            var inqNo = $("#inqNo").val(); // 숨겨진 입력 필드에서 inqNo 값을 가져옵니다.
            
            $.ajax({
                url: '/inquiry/delete',
                type: 'POST',
                data: { inqNo: inqNo },
                success: function(response) {
                    alert("문의글이 삭제되었습니다.");
                    locationProcess("/inquiry/inquiryList");
                },
                error: function(xhr, status, error) {
                    console.error("Status:", xhr.status);
                    console.error("Response Text:", xhr.responseText);
                    console.error("Error:", error);
                    alert("문의글 삭제 중 오류가 발생했습니다.");
                }
            });
        }
    });
    
    $("#updateFormBtn").on("click", function() {
        var inqNo = $("#inqNo").val(); // 숨겨진 입력 필드에서 inqNo 값을 가져옵니다.
        window.location.href = '/inquiry/updateForm/' + inqNo; // 수정 폼으로 이동
    });

    $("#cancelFormBtn").on("click", function() {
        if (confirm("목록으로 이동합니다")) {
            window.location.href = "/inquiry/inquiryList";
        }
    });

	const template = (result) => {
		let $div = $('#answerInquiryList');
						 
		let $element = $('#item-template').clone().removeAttr('id');
		$element.attr("data-ansNo", result.ansNo);
		$element.addClass("answerText");
		$element.find('.name').append("관리자");
		$element.find('.cdate').append(getDateFormat(new Date(result.ansDate)));
		let answer = result.answer;
		//answer = answer.replace(/(\r\n|\r|\n)/g, "<br />");
		$element.find('.card-text').html(answer);

		$div.append($element);
	} 
	
	let inqNo = $("#inqNo").val();
	console.log(inqNo);
	$.getJSON("/admin/inquiry/all/"+inqNo, function(data) {
		template(data);
	});


});
