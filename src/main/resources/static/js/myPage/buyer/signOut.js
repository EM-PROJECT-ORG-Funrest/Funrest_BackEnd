document.getElementById('signOutButton').addEventListener('click', function () {
    var checkBox = document.getElementById('flexCheckChecked');
    var reasonSelect = document.getElementById('reasonSelect');
    if (!checkBox.checked) {
        alert('유의 사항 체크를 확인해 주세요');
    } else if (reasonSelect.value === "") {
        alert('탈퇴사유를 선택해 주세요!');
    } else {
        alert('탈퇴 처리가 완료되었습니다.');
        location.href = '/th/myPage/buyer/deleteSign';
    }
});


