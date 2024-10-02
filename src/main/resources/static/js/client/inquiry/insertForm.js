/* 
$(document).ready(function() {
    // 비밀글 체크박스 상태에 따라 비밀번호 입력란 표시/숨기기
    $('#inqSecret').change(function() {
        if ($(this).is(':checked')) {
            $('#passwordSection').show(); // 비밀번호 입력란 표시
            $('#passwordButtonSection').show(); // 설정 버튼 표시
        } else {
            $('#passwordSection').hide(); // 비밀번호 입력란 숨기기
            $('#passwordButtonSection').hide(); // 설정 버튼 숨기기
        }
    }).trigger('change'); // 페이지 로드 시 초기 상태에 따라 처리

    // 비밀번호 설정 버튼 클릭 시 비밀번호 길이 확인
    $('#passwordFormBtn').click(function() {
        var password = $('#inqPassword').val();
        if (password.length < 4) {
            $('#passwordLengthMessage').show();
        } else {
            $('#passwordLengthMessage').hide();
            alert('비밀번호가 설정되었습니다.');
        }
    }); */

const template = (data) => {
	let $div = $('#answerInquiryList');
	
	let $element = $('#item-template').clone().removeAttr('id');
	$element.attr("data-id", data.id);
	$element.addClass("InquiryAnswer");
	$element.find('.name').append(data.memberName);
	$element.find('.cdate').append(getDateFormat(new Date(data.cdate)));
	let 
	
}
    // '작성완료' 버튼 클릭 시 서버로 데이터 전송
    $("#saveFormBtn").on("click", function(event) {
        event.preventDefault(); // 기본 폼 제출 방지

        // 유효성 검사
        if (!chkData("#inqTitle", "제목을")) return;
        if (!chkData("#inqContent", "내용을")) return;
        if (!chkData("#inqCate", "카테고리를")) return;

        // 비밀글 선택 시 비밀번호 유효성 검사 추가
        if ($("#inqSecret").is(":checked")) {
            if (!chkData("#inqPassword", "비밀번호를")) return;
            if ($("#inqPassword").val().length < 4) {
                alert("비밀번호는 4자리 이상이어야 합니다.");
                return;
            }
        }

        // 폼 데이터를 객체로 생성 (Inquiry 엔티티에 맞게 수정)
        var insertForm = {
            member: { memberNo: $("#memberNo").val() }, 
            inqCate: $("#inqCate").val(),
            inqTitle: $("#inqTitle").val(),
            inqSecret: $("#inqSecret").is(":checked"),
            inqPassword: $("#inqPassword").val(),
            inqContent: $("#inqContent").val()
        };

        // 작성완료 버튼 비활성화 (중복 클릭 방지)
        $("#saveFormBtn").prop("disabled", true);

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
                $("#saveFormBtn").prop("disabled", false);  // 오류 발생 시 버튼 다시 활성화
            }
        });
    });

    // 초기화 버튼 클릭 시 폼 초기화
    $("#resetFormBtn").on("click", function(){
        $("#insertForm")[0].reset(); // 폼 리셋
        $("#saveFormBtn").prop("disabled", false);  // 리셋 시 작성완료 버튼도 다시 활성화
    });

    // 목록 버튼 클릭 시 문의 리스트로 이동
    $("#cancelFormBtn").on("click", function(){
        if(confirm("정말 취소하시겠습니까?")) {
            window.location.href = "/inquiry/inquiryList"; // 페이지 이동
        }
    });
