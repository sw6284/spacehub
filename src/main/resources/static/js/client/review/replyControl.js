const loadReplyFunctions = () => {
    // 폼 검증 함수 추가
    const checkForm = (selector, message) => {
        if ($(selector).val().trim() === "") {
            alert(message + " 입력해 주세요.");
            $(selector).focus();
            return false;
        }
        return true;
    };

    // 대댓글 템플릿 함수
    const replyTemplate = (data, reviewId) => {
        let $div = $(`#reviewList div[data-id=${reviewId}] .replyList`);
        let $element = $('<div class="card mb-2"></div>').attr("data-id", data.replyNo);
        $element.append(`<div class="card-header"><span class="name">${data.member}</span><span class="cdate float-right">${getDateFormat(new Date(data.replyDate))}</span></div>`);
        let body = data.replyContent.replace(/(\r\n|\r|\n)/g, "<br />");
        $element.append(`<div class="card-body"><p class="card-text">${body}</p><button class="btn btn-primary replyUpdateFormBtn">수정</button><button class="btn btn-danger replyDeleteBtn">삭제</button></div>`);
        $div.append($element);
        console.log("Reply added:", $element); // 콘솔 로그 추가
    };

    // 대댓글 등록
    $(document).off("click", ".replyInsertBtn").on("click", ".replyInsertBtn", function(){ 
        let $form = $(this).closest(".replyForm");
        let reviewId = $(this).closest(".review").data("id");
        if (!checkForm($form.find(".replyMember"), "작성자를")) return; 
        else if (!checkForm($form.find(".replyContent"), "내용을")) return;
        else {
            // 버튼 비활성화
            $(this).prop("disabled", true);
            $.ajax({
                url: "/review/replyInsert", // 전송 URL
                method: "POST", // 전송 시 method 방식
                contentType: "application/json", // Content-Type 헤더 설정
                data: JSON.stringify({
                    member: $form.find(".replyMember").val(),
                    replyContent: $form.find(".replyContent").val(),
                    review: {
                        revNo: reviewId
                    }
                }),
                dataType: "json"
            }).done(function(data){    
                if(data != ""){
                    console.log("Inserted reply data:", data); // 삽입된 대댓글 데이터를 로그로 출력
                    replyTemplate(data, reviewId); // 새로운 대댓글을 리스트에 추가
                    alert("대댓글 등록이 완료되었습니다.");
                    $form[0].reset();
                }
            }).fail(function(jqXHR, textStatus, errorThrown){
                alert("시스템에 문제가 있어 잠시 후 다시 진행해 주세요.");
                console.error("Error details:", textStatus, errorThrown); // 에러 상세 로그 출력
            }).always(function() {
                // 버튼 다시 활성화
                $(this).prop("disabled", false);
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
};
