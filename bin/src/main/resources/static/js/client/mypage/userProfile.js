$(document).ready(function() {
    const isLoggedIn = /*[[${isLoggedIn}]]*/ false;

    if (!isLoggedIn) {
        alert("로그인 후 이용 가능합니다.");
		locationProcess("/auth/login");
    }
});