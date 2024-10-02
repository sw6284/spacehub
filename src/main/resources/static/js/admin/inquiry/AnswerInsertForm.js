/*$(document).ready(function() {
    // '작성완료' 버튼 클릭 시 서버로 데이터 전송
    $("#saveFormBtn").on("click", function(event) {
        event.preventDefault(); // 기본 폼 제출 방지

        // 유효성 검사
        if (!chkData("#inqTitle", "제목을")) return;
        if (!chkData("#inqContent", "내용을")) return;
        if (!chkData("#inqCate", "카테고리를")) return;

        // 폼 데이터를 객체로 생성 (Inquiry 엔티티에 맞게 수정)
        var insertForm = {
            member: { memberNo: $("#memberNo").val() }, 
            inqCate: $("#inqCate").val(),
            inqTitle: $("#inqTitle").val(),
            inqSecret: $("#inqSecret").is(":checked"),
            inqPassword: $("#inqPassword").val(),
            inqContent: $("#inqContent").val()
        };

        // 서버로 데이터 전송
        $.ajax({
            url: '/inquiry/insert',  // 서버의 저장 엔드포인트
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(insertForm),  // JSON으로 직렬화하여 전송
            success: function(response) {
                alert('문의가 성공적으로 저장되었습니다.');
                window.location.href = "/inquiry/inquiryList";  // 성공 시 페이지 이동
            },
            error: function(error) {
                alert('저장 중 오류가 발생했습니다.');
            }
        });
    }); */
/*
	$(document).on("click", "#AnswerinsertForm", function(){ 
		if (!checkForm("#answerContent","내용을"))  return; 
		else {
			 JSON.stringify(): JavaScript 값이나 객체를 JSON 문자열로 변환. 
			$.ajax({
				url : "/admin/inquiry/",     //전송 url
				method : "post",    // 전송 시 method 방식
				headers : {
					"Content-Type":"application/json"
				},
				data : JSON.stringify({
					nickname:$("#nickname").val(),
					body:$("#body").val(),
					article:{
						no:no
					}
				}),
				dataType:"json"
			}).done(function(data){	
				if(data!=""){
					alert("답변 등록이 완료되었습니다.");
					//let data = JSON.parse(result);
					dataReset();
					template(data);
				}
			}).fail(function(){
				alert("시스템에 문제가 있어 잠시 후 다시 진행해 주세요.");
			});	
		}
	}); 

	// 답변 완료 버튼 클릭 시 내용이 저장된 상세페이지로 다시 화면 노출
	  $("#saveFormBtn").on("click", function(){
	      $("#AnswerinsertForm").submit();
	  });

	  
    // 초기화 버튼 클릭 시 폼 초기화
    $("#resetFormBtn").on("click", function(){
        $("#insertForm")[0].reset(); // 폼 리셋
    });

    // 목록 버튼 클릭 시 문의 리스트로 이동
    $("#cancelFormBtn").on("click", function(){
        if(confirm("정말 취소하시겠습니까?")) {
            window.location.href = "/inquiry/inquiryList"; // 페이지 이동
        }
    });
	
	

*/