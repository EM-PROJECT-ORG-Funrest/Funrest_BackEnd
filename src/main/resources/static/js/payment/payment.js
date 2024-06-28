// Daum 우편번호 서비스 사용
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
            document.getElementById("payment-road-addr").value = roadAddr;
        }
    }).open({
        // 창 띄우는 위치 지정
        // left: (window.screen.width / 2) - (width / 2),
        // top: (window.screen.height / 2) - (height / 2)\
        // https://postcode.map.daum.net/guide
    });
}

// 번호 유효성 검사 함수
function telValidChk(tel) {
    const pattern = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/;

    if(pattern.test(tel.value) === false) { 
        tel.classList.add("is-invalid");
        return false;
    } else {
        tel.classList.remove("is-invalid");
        return true;
    }
}

// 결제 폼 제출 시 유효성 검사
function paymentValidChk(event) {
    event.preventDefault(); // 폼 제출 시 페이지 새로고침(기본동작) 방지
    const name = document.getElementById("payment-name");
    const phone = document.getElementById("payment-phone");
    const postCode = document.getElementById("payment-post-code");
    const addrDetail = document.getElementById('payment-addr-detail');
    const chkBox = document.getElementById("pay-chkbox");
    // 이름 필드 유효성 검사
    if(name.value === ""){
        name.classList.add("is-invalid");
        return false;
    } else {
        name.classList.remove("is-invalid");
    }
    // 연락처 필드 유효성 검사
    if(phone.value === "" || !telValidChk(phone)){
        phone.classList.add("is-invalid");
        return false;
    } else {
        phone.classList.remove("is-invalid");
    }
    // 우편번호 필드 유효성 검사
    if(postCode.value === "") {
        postCode.classList.add("is-invalid");
        return false;
    } else {
        postCode.classList.remove("is-invalid");
    }
    // 상세 주소 필드 유효성 검사
    if(addrDetail.value === "") {
        addrDetail.classList.add("is-invalid");
        return false;
    } else {
        addrDetail.classList.remove("is-invalid");
    }
    // 체크박스 유효성 검사
    if(!chkBox.checked){
        window.alert("결제 동의 항목에 체크해 주세요.");
        return false;
    }
    // 결제 폼 유효성 검사 성공 메시지
    console.log("성공");
    return true;
}

// 총 결제 금액 표시 함수
function updateTotalPrice(){
    var productPrice = document.getElementById("productPrice").innerText.replace(/,/g,'');
    var countSelect = document.getElementById("countSelect").value;
    var deliveryPay = parseInt(document.getElementById("deliveryPay").value);
    var totalPrice = productPrice * countSelect;
    var totalPayment = totalPrice + deliveryPay;
    document.getElementById("totalPrice").innerText = totalPrice.toLocaleString()+"원";
    document.getElementById("totalPayment").innerText = totalPayment.toLocaleString()+"원";
}

// 결제하기 버튼 누르면 portOne api 작동
document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("paymentBtn").addEventListener("click", function(event) {
        event.preventDefault(); // 기본 동작 방지
        if(paymentValidChk(event)) {
             goToPay();
        }
    });
});

function goToPay() {

    IMP.init("imp83318333");

    var payMethod = document.querySelector(".form-check-input").value;
    var merchantUid = document.getElementById("payment-name").value + new Date().getTime();
    var productName = document.getElementById("product-Name").textContent;
    var orderCnt = parseInt(document.querySelector(".count-select").value);
    var productPrice = parseInt(document.getElementById("productPrice").innerText.replace(/,/g, ''), 10);
    var deliveryPay = parseInt(document.getElementById("deliveryPay").value);
    var amount = productPrice * orderCnt + deliveryPay;
    var buyerTel = document.getElementById("payment-phone").value;
    var buyerName = document.getElementById("payment-name").value;
    var buyerAddr = document.getElementById("payment-road-addr").value+" "+document.getElementById("payment-addr-detail").value
    var buyerPostcode = document.getElementById("payment-post-code").value;
    var proCode = parseInt(document.getElementById("proCode").value, 10); // proCode를 정수형으로 변환
    var userId = document.getElementById("userId").value;
    var orderState = '결제완료';

    IMP.request_pay (
    {
        pg: "html5_inicis.INIpayTest",
        pay_method: payMethod,
        merchant_uid: merchantUid,
        name: productName,
        amount: amount,
        buyer_tel: buyerTel,
        buyer_name: buyerName,
        buyer_addr: buyerAddr,
        buyer_postcode: buyerPostcode
    },
    function(resp) {
        if(resp.success) {
            var msg='결제 성공했습니다'
            console.log(resp);
            // form 전 여기에서 값 넣기

            var result={
               "buyerAddr": buyerAddr,
               "buyerName": buyerName,
               "buyerPostcode": buyerPostcode,
               "buyerTel": buyerTel,
               "impUid" : resp.imp_uid,
               "merchantUid" : merchantUid,
               "orderCnt" : orderCnt,
               "orderDate" : new Date().toISOString().slice(0,10),
               "orderMethod" : resp.pay_method+" "+resp.card_name,
               "proCode" : proCode, // 정수형으로 변환된 proCode 사용
               "totalAmount" : amount,
               "userId" : userId
            }
            console.log(result);

            $.ajax({
                url: '/api/complete',
                method: 'POST',
                contentType : "application/json",
                data: JSON.stringify(result), // result 객체를 JSON 문자열로 변환하여 전송

                success: function (resp) {
                    var redirectUrl = '/th/payment/paymentHistory/' + userId + '?orderState=' + encodeURIComponent(orderState);
                    alert("결제 내역 페이지로 넘어갑니다.");
                    window.location.href = redirectUrl;
                },
                error: function (err) {
                    alert("결제 처리 중 문제가 발생하였습니다.");
                    console.log("errorDetail : " ,JSON.stringify(err,null,2)); // 에러 출력
                }
            });
        } else {
            var msg ='결제 실패하였습니다.';
            msg += '\n에러내용 : ' + resp.error_msg;
        }
        alert(msg);
    }
    );
}


