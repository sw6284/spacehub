$("#userProfileUpdateFormBtn").on("click", function(){
    locationProcess("/myPage/checkPassword");
});

$("#userDeleteBtn").on("click", function(){
	locationProcess("/myPage/userDeleteForm");
});
