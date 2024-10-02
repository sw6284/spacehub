/*
   // 글쓰기 버튼 제어
   $("#insertFormBtn").click(function(){
       locationProcess("/admin/inquiry/AnswerinsertForm");
   }); */
   
   // 제목 클릭 시 상세 페이지 이동을 위한 처리 이벤트
   $(".goDetail").click(function(){
       let inqNo = $(this).closest("tr").find(".inquiryNo").text();
       console.log("글번호 : " + inqNo);
       
       if (inqNo) {
           location.href = "/admin/inquiry/" + inqNo; // location.href 사용
       } else {
           console.error("글번호를 찾을 수 없습니다.");
       }
   });
