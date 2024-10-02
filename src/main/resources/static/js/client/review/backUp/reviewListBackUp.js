//<!-- memberId, reservationInd 추적 추가 -->


$(document).ready(function() {
    // 날짜 형식 함수 추가
    const getDateFormat = (date) => {
        const year = date.getFullYear();
        const month = ('0' + (date.getMonth() + 1)).slice(-2);
        const day = ('0' + date.getDate()).slice(-2);
        const hours = ('0' + date.getHours()).slice(-2);
        const minutes = ('0' + date.getMinutes()).slice(-2);
        const seconds = ('0' + date.getSeconds()).slice(-2);
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    };
	// 이 GET 요청을 사용하여 memberId를 설정합니다.
	$(document).ready(function() {
	    $.ajax({
	        url: '/review/getMemberId',
	        method: 'GET',
	        success: function(data) {
	            $('#memberId').val(data);
	            $('#member').val(data);
	        },
	        error: function() {
	            alert('회원 정보를 가져오는 데 실패했습니다.');
	        }
	    });
	});

    // 폼 검증 함수 추가
    const checkForm = (selector, message) => {
        if ($(selector).val().trim() === "") {
            alert(message + " 입력해 주세요.");
            $(selector).focus();
            return false;
        }
        return true;
    };

    // 리뷰 템플릿 함수
    const template = (data) => {
        let $div = $('#reviewList');
        let $element = $('#item-template').clone().removeAttr('id').show(); // 요소를 복제하고 표시
        $element.attr("data-id", data.revNo);
        $element.addClass("review");
        $element.find('.name').text(data.member.memberName); // memberName 참조
        $element.find('.cdate').text(getDateFormat(new Date(data.revDate)));
        let body = data.revContent;
        body = body.replace(/(\r\n|\r|\n)/g, "<br />");
        $element.find('.card-text').html(body);
        $div.append($element); 
        console.log("Review added:", $element); // 콘솔 로그 추가
    };

    // 폼 데이터 초기화 함수
    const dataReset = () => {
        $("#reviewForm").each(function(){
            this.reset();
        });
    };

    // 리뷰 목록 로드
    let spaceId = 1; // $("#spaceId").val();
    console.log("Space ID:", spaceId);
    if (!spaceId) {
        alert("spaceId가 설정되지 않았습니다.");
        return;
    }
    $.getJSON("/review/reviewList/" + spaceId, function(result) { 
        console.log("Reviews loaded:", result);
        for(let value of result){
            console.log("Review data:", value);
            template(value);
        }
    }).fail(function() {
        alert("리뷰 목록을 불러오는데 실패하였습니다. 잠시 후에 다시 시도해 주세요.");
    });

	// 리뷰 등록
	$(document).off("click", "#reviewInsertBtn").on("click", "#reviewInsertBtn", function(){ 
	    if (!checkForm("#member","작성자를")) return; 
	    else if (!checkForm("#revContent","내용을")) return;
	    else {
	        // 버튼 비활성화
	        $(this).prop("disabled", true);
	        $.ajax({
	            url: "/review/reviewInsert", // 전송 URL
	            method: "POST", // 전송 시 method 방식
	            contentType: "application/json", // Content-Type 헤더 설정
	            data: JSON.stringify({
	                member: {
	                    memberName: $("#member").val() // memberName 설정
	                },
	                revContent: $("#revContent").val(),
	                space: {
	                    spNo: $("#spaceId").val()
	                },
	                reservation: {
	                    resNo: $("#resNo").val() // 숨겨진 필드에서 resNo 값 가져오기
	                }
	            }),
	            dataType: "json"
	        }).done(function(data){    
	            if(data != ""){
	                console.log("Inserted review data:", data); // 삽입된 리뷰 데이터를 로그로 출력
	                template(data); // 새로운 리뷰를 리스트에 추가
	                alert("리뷰 등록이 완료되었습니다.");
	                dataReset();
	            }
	        }).fail(function(jqXHR, textStatus, errorThrown){
	            alert("시스템에 문제가 있어 잠시 후 다시 진행해 주세요.");
	            console.error("Error details:", textStatus, errorThrown); // 에러 상세 로그 출력
	        }).always(function() {
	            // 버튼 다시 활성화
	            $("#reviewInsertBtn").prop("disabled", false);
	        });    
	    }
	});


    // 리뷰 삭제
    $(document).off("click", ".reviewDeleteBtn").on("click", ".reviewDeleteBtn", function(e){ 
        e.preventDefault();
        let reviewDeleteDataArea = $(this).parents("div.review");
        let reviewId = reviewDeleteDataArea.data("id");
        console.log("Review ID to delete:", reviewId); // 삭제할 리뷰 ID를 로그로 출력
        if (confirm("선택하신 리뷰를 삭제하시겠습니까?")) {
            $.ajax({
                url: '/review/'+reviewId, 
                method: 'DELETE',   // method - get(조회-R)/post(입력-C)/put(수정-U)/delete(삭제-D) 명시
                dataType: 'text'
            }).done(function(){    
                reviewDeleteDataArea.remove();
            }).fail(function(){
                alert("시스템에 문제가 있어 잠시 후 다시 진행해 주세요.");
            });    
        } 
    });

    $(document).off("click", ".replyToggleBtn").on("click", ".replyToggleBtn", function() {
        let $form = $(this).closest(".card").find(".replyForm");
        $form.toggle();
    });

    // 대댓글 템플릿 함수
    const replyTemplate = (data, reviewId) => {
        let $div = $(`#reviewList div[data-id=${reviewId}] .replyList`);
        let $element = $('<div class="card mb-2"></div>').attr("data-id", data.replyNo);
        $element.append(`<div class="card-header"><span class="name">${data.member.memberName}</span><span class="cdate float-right">${getDateFormat(new Date(data.replyDate))}</span></div>`);
        let body = data.replyContent.replace(/(\r\n|\r|\n)/g, "<br />");
        $element.append(`<div class="card-body"><p class="card-text">${body}</p><button class="btn btn-primary replyUpdateFormBtn">수정</button><button class="btn btn-danger replyDeleteBtn">삭제</button></div>`);
        $div.append($element);
        console.log("Reply added:", $element); // 콘솔 로그 추가
    };

    $(document).off("click", ".replyInsertBtn").on("click", ".replyInsertBtn", function() {
        let $button = $(this);
        let $form = $button.closest(".replyForm");
        let reviewId = $button.closest(".review").data("id");
        if (!checkForm($form.find(".replyMember"), "작성자를")) return;
        else if (!checkForm($form.find(".replyContent"), "내용을")) return;
        else {
            // 버튼 비활성화
            $button.prop("disabled", true);
            $.ajax({
                url: "/review/replyInsert", // 전송 URL
                method: "POST", // 전송 시 method 방식
                contentType: "application/json", // Content-Type 헤더 설정
                data: JSON.stringify({
                    member: {
                        memberName: $form.find(".replyMember").val() // memberName 설정
                    },
                    replyContent: $form.find(".replyContent").val(),
                    review: {
                        revNo: reviewId
                    }
                }),
                dataType: "json"
            }).done(function(data) {
                if (data != "") {
                    console.log("Inserted reply data:", data); // 삽입된 대댓글 데이터를 로그로 출력
                    replyTemplate(data, reviewId); // 새로운 대댓글을 리스트에 추가
                    alert("대댓글 등록이 완료되었습니다.");
                    $form[0].reset();
                }
            }).fail(function(jqXHR, textStatus, errorThrown) {
                alert("시스템에 문제가 있어 잠시 후 다시 진행해 주세요.");
                console.error("Error details:", textStatus, errorThrown); // 에러 상세 로그 출력
            }).always(function() {
                // 버튼 다시 활성화
                $button.prop("disabled", false);
            });
        }
    });

    // 대댓글 수정 폼 열기
    $(document).off("click", ".replyUpdateFormBtn").on("click", ".replyUpdateFormBtn", function(e){ 
        e.preventDefault();
        let card = $(this).closest(".card");
        let replyId = card.data("id");
        let member = card.find(".name").text();
        let content = card.find(".card-text").html().replace(/<br\s*\/?>/g, '\n');
        let $form = $(`
            <form class="replyUpdateForm mt-3">
                <div class="row mb-3">
                    <label for="replyMember" class="col-sm-1 col-form-label">작성자</label>
                    <div class="col-sm-3">
                        <input type="text" name="replyMember" class="replyMember form-control" maxlength="8" value="${member}" readonly />
                    </div>
                </div>
                <div class="row mb-3">
                    <label for="replyContent" class="col-sm-1 col-form-label">내용</label>
                    <div class="col-sm-11">
                        <textarea name="replyContent" class="replyContent form-control" rows="2">${content}</textarea>
                    </div>
					                </div>
					                <button type="button" class="btn btn-secondary replyUpdateBtn">수정 완료</button>
					                <button type="button" class="btn btn-secondary replyCancelBtn">취소</button>
					            </form>
					        `);
					        card.after($form);
					        card.hide();
					    });

					    // 대댓글 수정 취소
					    $(document).off("click", ".replyCancelBtn").on("click", ".replyCancelBtn", function(){
					        let $form = $(this).closest(".replyUpdateForm");
					        $form.prev(".card").show();
					        $form.remove();
					    });

					    // 대댓글 수정
					    $(document).off("click", ".replyUpdateBtn").on("click", ".replyUpdateBtn", function(){
					        let $form = $(this).closest(".replyUpdateForm");
					        let replyId = $form.prev(".card").data("id");
					        if (!checkForm($form.find(".replyContent"), "내용을")) return;
					        else {
					            $.ajax({
					                url: `/review/reply/${replyId}`,
					                method: 'PUT',
					                headers: { 
					                    "Content-Type": "application/json",
					                },
					                data: JSON.stringify({
					                    replyContent: $form.find(".replyContent").val()
					                }), 
					                dataType: 'json'
					            }).done(function(data){
					                if(data != ""){
					                    alert("대댓글 수정이 완료되었습니다.");
					                    let card = $form.prev(".card");
					                    card.find(".card-text").html(data.replyContent.replace(/(\r\n|\r|\n)/g, "<br />"));
					                    card.show();
					                    $form.remove();
					                }
					            }).fail(function(){
					                alert("시스템에 문제가 있어 잠시 후 다시 진행해 주세요.");
					            });
					        }
					    });

					    // 대댓글 삭제
					    $(document).off("click", ".replyDeleteBtn").on("click", ".replyDeleteBtn", function(e){ 
					        e.preventDefault();
					        let replyDeleteDataArea = $(this).closest(".card");
					        let replyId = replyDeleteDataArea.data("id");
					        console.log("Reply ID to delete:", replyId); // 삭제할 대댓글 ID를 로그로 출력
					        if (confirm("선택하신 대댓글을 삭제하시겠습니까?")) {
					            $.ajax({
					                url: `/review/reply/${replyId}`, 
					                method: 'DELETE',   // method - get(조회-R)/post(입력-C)/put(수정-U)/delete(삭제-D) 명시
					                dataType: 'text'
					            }).done(function(){    
					                replyDeleteDataArea.remove();
					            }).fail(function(){
					                alert("시스템에 문제가 있어 잠시 후 다시 진행해 주세요.");
					            });    
					        } 
					    });
					});




