// 회원관리 테이블에서 체크된 회원 삭제하는 기능
document.getElementById('deleteButton').addEventListener('click', function() {
    const checkboxes = document.querySelectorAll('input[name="members"]:checked');
    const ids = Array.from(checkboxes).map(checkbox => checkbox.value); // 체크된 회원 id를 배열 ids 에 저장

    if (ids.length === 0) {
        alert("삭제할 회원을 선택하세요.");
        return;
    }

    fetch('/api/deleteMembers', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ ids: ids }) // ids: 삭제할 회원들의 userId 배열
    })
    .then(response => {
        if (response.ok) {
            alert('삭제되었습니다.');
            window.location.reload(); // 페이지 새로고침하여 변경 사항을 반영
        } else {
            alert('삭제에 실패했습니다.');
        }
    })
    .catch(error => console.log('Error: ', error));
});