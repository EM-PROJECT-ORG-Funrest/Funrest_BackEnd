document.write('<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>');
function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분
            // 도로명 주소의 노출 규칙에 따라 주소를 표시
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기
            var roadAddr = data.roadAddress; // 도로명 주소 변수

            // 우편번호와 주소 정보를 해당 필드에 넣음.
            document.getElementById("payment-post-code").value = data.zonecode;
            document.getElementById("payement-road-addr").value = roadAddr;
        }
    }).open({
        // 창 띄우는 위치 지정
        // left: (window.screen.width / 2) - (width / 2),
        // top: (window.screen.height / 2) - (height / 2)\
        // https://postcode.map.daum.net/guide
    });
}

function paymentValidChk() {
    const name = document.getElementById("payment-name");
    const phone = document.getElementById("payment-phone");
    const postCode = document.getElementById("payment-post-code");
    const isChked = document.querySelector('#pay-chk').checked;

    if(!checkExistData(name.value)){
        //이름 null chk
        name.classList.add("invalid-feedback");
        return false;
    } else if(!checkValidTel(phone)){
        //연락처 null & valid chk
        phone.classList.remove("invalid-feedback");
        phone.classList.add("invalid-feedback");
        return false;
    } else if(!checkExistData(postCode.value)){
        //우편번호 null chk
        phone.classList.remove("invalid-feedback");
        postCode.classList.add("invalid-feedback");
        return false;
    } else if(!isChked){
        //pay-radio boolean chk -> alert
        postCode.classList.remove("invalid-feedback");
        alert("주문 내용 확인과 회원의 개인정보 이용 및 제공, 결제에 동의해 주세요.");
        return false;
    }

    return true;
}

function checkExistData(value) {
    //공란 항목 체크
    if (value == "") {
        return false;
    }
    return true;
}

function checkValidTel(el) {
    if(!checkExistData(el.value)){
        return false;
    }

    const pattern = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/;

    if(pattern.test(el.value) === false) { 
        return false;
    } else {
        return true;
    }
}