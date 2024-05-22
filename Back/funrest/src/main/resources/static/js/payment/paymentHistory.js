document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    const searchInput = document.getElementById('searchInput');

    form.addEventListener('submit', function(event) {
        event.preventDefault(); // 폼 제출 방지

        if (!searchInput.value.trim()) {
            alert('검색어를 입력해주세요!'); // 입력값이 없을 때 알림
        } else {
            // 여기에 검색을 처리하는 코드를 추가할 수 있습니다.
            // 예: 서버로 검색어를 보내고 결과를 받아오는 등의 동작
        }
    });
});