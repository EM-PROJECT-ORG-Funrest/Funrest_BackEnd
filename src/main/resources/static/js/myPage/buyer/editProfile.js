function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분
            // 도로명 주소의 노출 규칙에 따라 주소를 표시
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기
            var roadAddr = data.roadAddress; // 도로명 주소 변수

            // 우편번호와 주소 정보를 해당 필드에 넣음.
            document.getElementById("edit-post-code").value = data.zonecode;
            document.getElementById("edit-road-addr").value = roadAddr;
        }
    }).open({
        // 창 띄우는 위치 지정
        // left: (window.screen.width / 2) - (width / 2),
        // top: (window.screen.height / 2) - (height / 2)\
        // https://postcode.map.daum.net/guide
    });
}

function telValidChk() {
    const tel = document.getElementById("tel");
    const pattern = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/;

    if(tel.value === "") {
        document.getElementById("validationMessageTel").textContent = "연락처가 유효하지 않습니다.";
        tel.classList.add("is-invalid");
        return false;
    } else if(pattern.test(tel.value) === false) { 
        document.getElementById("validationMessageTel").textContent = "연락처가 유효하지 않습니다.";
        tel.classList.add("is-invalid");
        return false;
    } else {
        tel.classList.remove("is-invalid");
        return true;
    }
}
function editProfile(event) {
    event.preventDefault();

    const postCode = document.getElementById("edit-post-code");
    const tel = document.getElementById("tel");

    if(postCode.value === "" && tel.value === ""){
        window.alert("변경을 원하시는 항목에 값을 입력해 주세요.");
        return false;
    } 
        
    // 휴대폰 번호가 인증되지 않은 경우
    if (!isTelVerified(tel.value)) {
        document.getElementById("validationMessageTel").textContent = "연락처 인증을 완료해 주세요.";
        tel.classList.add("is-invalid");
        return false;
    }

    //this.submit();
}