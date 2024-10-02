document.getElementById("spaceUpdateFormBtn").addEventListener("click", function() {
    var spNo = document.querySelector('input[name="spNo"]').value;
    window.location.href = "/admin/space/updateForm/" + spNo;
});

document.getElementById("spaceListBtn").addEventListener("click", function() {
    window.location.href = "/admin/space/spaceList";
});

document.getElementById("spaceDeleteBtn").onclick = function(event) {
    event.preventDefault();  // 기본 폼 제출 동작 방지
    if (confirm("정말 삭제하시겠습니까?")) {
        const form = this.closest("form");
        form.submit();  // 폼 제출
    }
};
