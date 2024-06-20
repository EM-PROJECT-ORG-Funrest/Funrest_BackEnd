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

// 신청 프로젝트 관리 테이블에서 체크된 프로젝트 수정하는 기능
document.getElementById('UpdateBtn').addEventListener('click', function() {
    console.log("test");
    const checkboxes = document.querySelectorAll('input[name="projectsBefore"]:checked');
    const proCodes = Array.from(checkboxes).map(checkbox => checkbox.value); // 체크된 proCode 를 배열 proCodes 에 저장

    if (proCodes.length === 0) {
            alert("수정할 프로젝트를 선택하세요.");
            return;
        }

    if (proCodes.length >= 2) {
        alert("한번에 하나의 프로젝트만 수정할 수 있습니다.");
        return;
    }

    const proCode = proCodes[0]; // 체크된 하나의 proCode
    console.log(proCode);
    window.location.href = `/th/admin/editProject?proCode=${proCode}`; // 수정 페이지로 리다이렉트
});

// 신청 프로젝트 관리 테이블에서 체크된 프로젝트 승인하는 기능
document.getElementById('PermitBtn').addEventListener('click', function() {
    const checkboxes = document.querySelectorAll('input[name="projectsBefore"]:checked');
    const proCodes = Array.from(checkboxes).map(checkbox => checkbox.value); // 체크된 proCode 를 배열 proCodes 에 저장

    if (proCodes.length === 0) {
        alert("승인할 프로젝트를 선택하세요.");
        return;
    }

    fetch('/api/permitProjects', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ proCodes: proCodes }) // proCodes: 승인할 프로젝트의 proCode 배열
    })
    .then(response => {
        if (response.ok) {
            alert('승인되었습니다.');
            window.location.reload(); // 페이지 새로고침하여 변경 사항을 반영
        } else {
            alert('승인에 실패했습니다.');
        }
    })
    .catch(error => console.error('Error: ', error));
});

// 승인된 프로젝트 관리 테이블에서 체크된 프로젝트 삭제하는 기능
document.getElementById('PermitDeleteBtn').addEventListener('click', function() {
    const checkboxes = document.querySelectorAll('input[name="projectsAfter"]:checked');
    const proCodes = Array.from(checkboxes).map(checkbox => checkbox.value); // 체크된 proCode 를 배열 proCodes 에 저장

    if (proCodes.length === 0) {
        alert("삭제할 프로젝트를 선택하세요.");
        return;
    }

    fetch('/api/permitDeleteProjects', {
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

// 승인된 프로젝트 관리 테이블에서 체크된 프로젝트 승인 취소하는 기능
document.getElementById('PermitCancelBtn').addEventListener('click', function() {
    const checkboxes = document.querySelectorAll('input[name="projectsAfter"]:checked');
    const proCodes = Array.from(checkboxes).map(checkbox => checkbox.value); // 체크된 proCode 를 배열 proCodes 에 저장

    if (proCodes.length === 0) {
        alert("승인 취소할 프로젝트를 선택하세요.");
        return;
    }

    fetch('/api/permitCancelProjects', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ proCodes: proCodes }) // proCodes: 승인할 프로젝트의 proCode 배열
    })
    .then(response => {
        if (response.ok) {
            alert('승인 취소되었습니다.');
            window.location.reload(); // 페이지 새로고침하여 변경 사항을 반영
        } else {
            alert('승인 취소에 실패했습니다.');
        }
    })
    .catch(error => console.error('Error: ', error));
});