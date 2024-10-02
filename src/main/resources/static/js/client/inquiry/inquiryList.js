
   // 글쓰기 버튼 제어
   $("#insertFormBtn").click(function(){
       locationProcess("/inquiry/insertForm");
   });
   
   // 제목 클릭 시 상세 페이지 이동을 위한 처리 이벤트
   $(".goDetail").click(function(){
       let no = $(this).closest("tr").find(".inquiryNo").text();
       console.log("글번호 : " + no);
       
       if (no) {
           location.href = "/inquiry/" + no; // location.href 사용
       } else {
           console.error("글번호를 찾을 수 없습니다.");
       }
   });
   
   /* 페이징 처리 */
   $(".page-item a").on("click", function(e){
	e.preventDefault();
	$("#page").val($(this).data("number"));
	actionProcess("#searchForm","get","/inquiry/inquiryList");
	
   });
