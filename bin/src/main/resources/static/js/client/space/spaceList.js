// spaceList.js

document.addEventListener('DOMContentLoaded', () => {
    // "자세히 보기" 버튼 클릭 이벤트를 설정
    document.querySelectorAll('.detailBtn').forEach(button => {
        button.addEventListener('click', (event) => {
            event.preventDefault();
            
            // 클릭된 버튼의 부모 요소에서 공간 ID를 가져오기
            const card = button.closest('.card');
            const spaceId = card.getAttribute('data-id');
            
            // 상세 페이지 URL로 이동
            if (spaceId) {
                window.location.href = `/space/detail/${spaceId}`;
            } else {
                console.error('Space ID not found.');
            }
        });
    });
});
