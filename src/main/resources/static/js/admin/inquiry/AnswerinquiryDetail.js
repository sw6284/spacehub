
/*  글쓰기 : insert / 글목록 : inquiryListBtn / 작성완료 : SaveFormBtn / 
초기화 : CancleFormBtn / 삭제 : inquiryDeleteBtn / 수정 : updateFormBtn */


/* */
$(document).ready(function() {

	$("#answerInquiryListBtn").on("click",function(){
		window.location.href = "/admin/inquiry/inquiryList";
	});
	// 초기화 버튼 클릭 시 폼 초기화
	$("#answerCancelBtn").on("click", function(){
	    $("#answerInsertForm")[0].reset(); // 폼 리셋
	 //   $("#saveFormBtn").prop("disabled", false);   리셋 시 작성완료 버튼도 다시 활성화
	});
	
});

const template = (result) => {
	let $div = $('#answerInquiryList');
					 
	let $element = $('#item-template').clone().removeAttr('id');
	$element.attr("data-ansNo", result.ansNo);
	$element.addClass("answerText");
	$element.find('.name').append(result.admNo);
	$element.find('.cdate').append(getDateFormat(new Date(result.ansDate)));
	let answer = result.answer;
	//answer = answer.replace(/(\r\n|\r|\n)/g, "<br />");
	$element.find('.card-text').html(answer);

	$div.append($element);
}

const dataReset = () => {
	$("#answerInsertForm").each(function(){
		this.reset();
	});
}	


let inqNo = $("#inqNo").val();
console.log(inqNo);
$.getJSON("/admin/inquiry/all/"+inqNo, function(data) {
	template(data);
});


$(document).on("click", "#answerInsertBtn", function(){ 
	if (!checkForm("#answer","내용을"))  return; 
	else {
		/* JSON.stringify(): JavaScript 값이나 객체를 JSON 문자열로 변환. */
		$.ajax({
			url : "/admin/inquiry/answerInsert",     //전송 url
			method : "post",    // 전송 시 method 방식
			headers : {
				"Content-Type":"application/json"
			},
			data : JSON.stringify({
				admNo:$("#admNo").val(),
				answer:$("#answer").val(),
				inquiry:{
					inqNo:inqNo
				}
			}),
			dataType:"json"
		}).done(function(data){	
			if(data!=""){
				alert("답변 등록이 완료되었습니다.");
				dataReset();
				template(data);
			}
		}).fail(function(){
			alert("시스템에 문제가 있어 잠시 후 다시 진행해 주세요.");
		});	
	}
}); 

$(document).on("click", ".answerUpdateFormBtn", function(e){ 
	e.preventDefault();
	$("#answerInsertForm .resetBtn").detach();
			
	let card = $(this).parents("div.answerText");
	$("#ansNo").val(card.attr("data-ansNo"));
	console.log("ansNo: "+card.attr("data-ansNo"));
	
	//$("#nickname").val(card.find(".card-header .name").html());
	//$("#nickname").prop("readonly", true);
		
	let answer = card.find(".card-text").html();
	//answer = answer.replace(/(<br>|<br\/>|<br \/>)/g, '\r\n'); 
	$("#answer").val(answer);
	
	$("#answerInsertForm button[type='button']").attr("id", "answerUpdateBtn");
	let resetButton = $("<button type='button' class='btn btn-secondary col-sm-1 resetBtn mx-2'>");
	resetButton.html("취소");
	$("#answerInsertForm .sendBtn").after(resetButton);

});



$(document).on("click", ".resetBtn", function(){ 
	dataReset();
	//$("#nickname").prop("readonly", false);
	$("#answerInsertForm button[type='button']").attr("id", "answerInsertBtn");
	$("#answerInsertForm button[type='button'].resetBtn").detach();
	$("#ansNo").val(0);
});


$(document).on("click", "#answerUpdateBtn", function(){
	let ansNo = $("#ansNo").val();	
	if (!checkForm("#answer","댓글내용을"))	return;
	else {
		
		$.ajax({
			url:'/admin/inquiry/'+ansNo,
			method:'put',
			headers: { 
				"Content-Type": "application/json",
			},
			data:JSON.stringify({
				answer:$("#answer").val()
			}), 
			dataType:'json'
		}).done(function(data){
			if(data != ""){
				alert("댓글 수정이 완료되었습니다.");
				dataReset();
				console.log(data.answer);
				$('#answerInquiryList').find("div[data-ansNo="+ansNo+"]").find(".card-text").html(data.answer.replace(/(\r\n|\r|\n)/g, "<br />"));
			}
		}).fail(function(){
			alert("시스템에 문제가 있어 잠시 후 다시 진행해 주세요.");
		});
	}
}); 

$(document).on("click", ".answerDeleteBtn", function(e){ 
	e.preventDefault();
	let answerDeleteDataArea = $(this).parents("div.answerText");
	let ansNo = answerDeleteDataArea.attr("data-ansNo");
	console.log(ansNo);
	if (confirm("선택하신 댓글을 삭제하시겠습니까?")) {
		$.ajax({
			url : '/admin/inquiry/'+inqNo+"/"+ansNo, 
			method : 'delete',   // method - get(조회-R)/post(입력-C)/put(수정-U)/delete(삭제-D) 명시
			dataType : 'text'
		}).done(function(){	
			//alert("댓글 삭제가 완료되었습니다.");
			answerDeleteDataArea.remove();
		}).fail(function(){
			alert("시스템에 문제가 있어 잠시 후 다시 진행해 주세요.");
		});	
	} 
});
