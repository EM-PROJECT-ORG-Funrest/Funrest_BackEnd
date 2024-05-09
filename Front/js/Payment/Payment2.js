document.addEventListener("DOMContentLoaded", function() {
    var form = document.querySelector('form');

    // 정보가 모두 입력되었는지 확인
    form.addEventListener('submit', function(event) {
        var nameInput = form.querySelector('.name input');
        var phoneInput = form.querySelector('.phone input');
        var postCodeInput = form.querySelector('#payment-post-code');
        var roadAddrInput = form.querySelector('#payment-road-addr');
        var detailAddrInput = form.querySelector('.addr input[type="text"]');


        if (!nameInput.value.trim()) {
            alert('성함을 입력해주세요.');
            event.preventDefault(); // 폼 전송 막기
        } else if(!phoneInput.value.trim()){
            alert('연락처를 입력해주세요.');
            event.preventDefault(); 
        }else if(!postCodeInput.value.trim()|| !roadAddrInput.value.trim()){
            alert('배송지 주소를 입력해주세요.');
            event.preventDefault(); 
        }else if(!detailAddrInput.value.trim()){
            alert('상세주소를 입력해주세요.');
            event.preventDefault(); 
        }
    });
});


document.addEventListener("DOMContentLoaded", function() {
    var phoneInput = document.querySelector('.phone input');

    // 입력된 값이 숫자인지 확인하는 함수
    function isNumericInput(event) {
        var key = event.keyCode;
        return ((key >= 48 && key <= 57) || (key >= 96 && key <= 105));
    }

    // 입력된 값이 숫자가 아니면 입력을 막는 함수
    function isModifierKey(event) {
        var key = event.keyCode;
        return (event.shiftKey === true || key === 35 || key === 36 || key === 37 || key === 39 || key === 46);
    }

    // 연락처 형식을 자동으로 변환하는 함수
    function autoFormatPhoneNumber(event) {
        if (isModifierKey(event)) {
            return;
        }

        var target = event.target;
        var input = event.target.value.replace(/\D/g, '').substring(0, 11); // 숫자 외의 문자 제거 및 길이 제한

        var area = input.substring(0, 3);
        var middle = input.substring(3, 7);
        var last = input.substring(7, 11);

        if (input.length > 6) {
            target.value = `${area}-${middle}-${last}`;
        } else if (input.length > 3) {
            target.value = `${area}-${middle}`;
        } else if (input.length > 0) {
            target.value = `${area}`;
        }
    }

    phoneInput.addEventListener('keydown', autoFormatPhoneNumber);
    phoneInput.addEventListener('keyup', autoFormatPhoneNumber);
    phoneInput.addEventListener('focus', autoFormatPhoneNumber);
    phoneInput.addEventListener('blur', autoFormatPhoneNumber);
});