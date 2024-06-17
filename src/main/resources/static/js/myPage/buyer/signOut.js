document.getElementById('signOutButton').addEventListener('click', function () {
    var checkBox = document.getElementById('flexCheckChecked');
    if (!checkBox.checked) {
        alert('유의 사항 체크를 확인해 주세요');
    }else if (reasonSelect.value === "") {
                 alert('탈퇴사유를 선택해 주세요!');
     }else {
         // 유의 사항을 체크했을 때 수행할 작업 추가 (예: 폼 제출, AJAX 호출 등)
         alert('탈퇴 처리가 완료되었습니다.');
         // 실제 탈퇴 처리를 수행하는 로직을 여기에 추가
    }
});



