//<!-- memberId, reservationInd 추적 추가 -->

//탬플릿 평점 추가

//답글 admin만 권한 부여, 회원정보 admin 확인 추가 작성자 admin 고정

/*admin 또는 user 둘 중 하나가 로그인되어 있으면 “회원 정보를 가져오는 데 실패했습니다.” 메시지가 나오지 않도록 수정
reply 작성자의 value를 "admin"으로 고정*/
//<!-- memberId, reservationInd 추적 추가 -->

//탬플릿 평점 추가

//답글 admin만 권한 부여, 회원정보 admin 확인 추가 작성자 admin 고정
$(document).ready(function() {
    // 로그인된 사용자가 admin인지 확인
    let isAdmin = false;
    $.ajax({
        url: '/review/getAdminInfo',
        method: 'GET',
        success: function(response) {
            isAdmin = response ? true : false;

            if (!isAdmin) {
                // admin이 아닌 경우 admin-only 클래스를 가진 요소 숨기기
                $('.admin-only').hide();
            }
        },
        error: function() {
            // 오류 발생 시 admin이 아닌 것으로 간주
            $('.admin-only').hide();
        }
    });

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
        $element.find('.score').text("평점: " + data.revScore); // 평점 표시
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
    let spaceId = $("#spaceId").val();
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
        else if (!$("input[name='revScore']:checked").val()) {
            alert("평점을 선택해 주세요.");
            return;
        } else {
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
                    },
                    revScore: $("input[name='revScore']:checked").val() // 선택된 평점 값 가져오기
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

    // 답글 템플릿 함수
    const replyTemplate = (data, reviewId) => {
        let $div = $(`#reviewList div[data-id=${reviewId}] .replyList`);
        let $element = $('<div class="card mb-2"></div>').attr("data-id", data.replyNo);
        $element.append(`<div class="card-header"><span class="name">admin</span><span class="cdate float-right">${getDateFormat(new Date(data.replyDate))}</span></div>`);
        let body = data.replyContent.replace(/(\r\n|\r|\n)/g, "<br />");
        $element.append(`<div class="card-body"><p class="card-text">${body}</p><button class="btn btn-primary replyUpdateFormBtn">수정</button><button class="btn btn-danger replyDeleteBtn">삭제</button></div>`);
        $div.append($element);
        console.log("Reply added:", $element); // 콘솔 로그 추가
    };

    $(document).off("click", ".replyInsertBtn").on("click", ".replyInsertBtn", function() {
        if (!isAdmin) {
            alert("관리자만 답글을 작성할 수 있습니다.");
            return;
        }

        let $button = $(this);
        let $form = $button.closest(".replyForm");
        let reviewId = $button.closest(".review").data("id");
        if (!checkForm($form.find(".replyContent"), "내용을")) return;
        else {
            // 버튼 비활성화
            $button.prop("disabled", true);
            $.ajax({
                url: "/review/replyInsert", // 전송 URL
                method: "POST", // 전송 시 method 방식
                contentType: "application/json", // Content-Type 헤더 설정
                data: JSON.stringify({
                    member: {
                        memberName: "admin" // memberName을 admin으로 고정
                    },
                    replyContent: $form.find(".replyContent").val(),
                    review: {
                        revNo: reviewId
                    }
                }),
                dataType: "json"
            }).done(function(data) {
                if (data != "") {
                    console.log("Inserted reply data:", data); // 삽입된 답글 데이터를 로그로 출력
                    replyTemplate(data, reviewId); // 새로운 답글을 리스트에 추가
                    alert("답글 등록이 완료되었습니다.");
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

				   

				   

				    // 답글 삭제
				    $(document).off("click", ".replyDeleteBtn").on("click", ".replyDeleteBtn", function(e){ 
				        e.preventDefault();
				        let replyDeleteDataArea = $(this).closest(".card");
				        let replyId = replyDeleteDataArea.data("id");
				        console.log("Reply ID to delete:", replyId); // 삭제할 답글 ID를 로그로 출력
				        if (confirm("선택하신 답글을 삭제하시겠습니까?")) {
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
