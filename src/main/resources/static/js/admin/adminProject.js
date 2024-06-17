// 신청 프로젝트 관리 테이블에서 체크된 프로젝트 삭제하는 기능
document.getElementById('deleteBtn').addEventListener('click', function() {
    const checkboxes = document.querySelectorAll('input[name="projectsBefore"]:checked');
    const proCodes = Array.from(checkboxes).map(checkbox => checkbox.value); // 체크된 proCode 를 배열 proCodes 에 저장

    if (proCodes.length === 0) {
        alert("삭제할 프로젝트를 선택하세요.");
        return;
    }

    fetch('/api/deleteProjects', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ proCodes: proCodes }) // proCodes: 삭제할 프로젝트의 proCode 배열
    })
    .then(response => {
        if (response.ok) {
            alert('삭제되었습니다.');
            window.location.reload(); // 페이지 새로고침하여 변경 사항을 반영
        } else {
            alert('삭제에 실패했습니다.');
        }
    })
    .catch(error => console.error('Error: ', error));
});