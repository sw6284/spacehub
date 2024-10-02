$(document).ready(function() {
    $('#updateFormBtn').click(function(event) {
        event.preventDefault(); 

        // 폼 데이터를 수집
        var formData = $('#inquiryUpdateForm').serialize();
        $.ajax({
            type: 'POST',
            url: '/inquiry/update', // 서버에서 데이터를 처리할 URL
            data: formData,
            success: function(response) {
                alert('문의가 성공적으로 수정되었습니다.');
                window.location.href = '/inquiry/inquiryList'; // 수정 후 목록 페이지로 이동
            },
            error: function(xhr, status, error) {
                alert('문의 수정 중 오류가 발생했습니다.');
                console.error('Error:', error);
            }
        });
    });

	$("#inquiryListBtn").on("click", function() {
	      if (confirm("수정을 취소하시겠습니까?")) {
	          window.location.href = "/inquiry/inquiryList";
	      }
	  });
});
