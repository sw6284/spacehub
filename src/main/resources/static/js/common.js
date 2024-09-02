const actionProcess = function(form, method, action){
	$(form).attr({
		"method" : method,
		"action" : action
	});
	$(form).submit();
}


const actionFileProcess = function(form, method, action){
	$(form).attr({
		"method":method,
		"enctype":"multipart/form-data",
		"action":action
	});
	$(form).submit();
}

function chkFile(item){
	let ext = $(item).val().split('.').pop().toLowerCase();
	if(jQuery.inArray(ext, ['gif', 'png', 'jpg']) == -1){
		alert('gif, png, jpg 파일만 업로드 할 수 있습니다.');
		item.val("");
		return false;
	}else{
		return true;
	};
}

const locationProcess = function(url){
	location.href = url;
}

/* 함수명: chkData(유효성 체크 대상, 메시지 내용) 
 * 출력영역: alert으로.
 * 예시 : if (!chkData("#keyword","검색어를")) return;
 * */ 
function chkData(item, msg) {
	if($(item).val().replace(/\s/g,"")=="") {
		alert(msg+" 입력해 주세요.");
		$(item).val("");
		$(item).focus();
		return false;
	} else {
		return true;
	}
}

/* 함수명: getDateFormat(날자 데이터) 
 * 설명 : dataValue의 값을 년-월-일 형식(예시: 2018-01-01)으로 반환.*/ 
function getDateFormat(dateValue){
	var year = dateValue.getFullYear();
	
	var month = dateValue.getMonth()+1;
	month = (month<10) ? "0"+month : month;
	
	var day = dateValue.getDate();
	day = (day<10) ? "0"+day : day;
	
	var result = year+"-"+month+"-"+day;
	return result;
}

