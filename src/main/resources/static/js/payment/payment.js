
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

function paymentValidChk(event) {
    event.preventDefault();
    //console.log("새로고침 그만");

    //이름 not null
    //연락처 not null & pattern
    //우편번호 not null
    //checkbox not null
    
    const name = document.getElementById("payment-name");
    const phone = document.getElementById("payment-phone");
    const postCode = document.getElementById("payment-post-code");
    const chkBox = document.getElementById("pay-chkbox");

    if(name.value === ""){
        name.classList.add("is-invalid");
        return false;
    } else {
        name.classList.remove("is-invalid");
    }

    if(phone.value === "" || !telValidChk(phone)){
        phone.classList.add("is-invalid");
        return false;
    } else {
        phone.classList.remove("is-invalid");
    }

    if(postCode.value === "") {
        postCode.classList.add("is-invalid");
        return false;
    } else {
        postCode.classList.remove("is-invalid");
    }

    if(!chkBox.checked){
        window.alert("결제 동의 항목에 체크해 주세요.");
        return false;
    }

    console.log("성공");
    return true;
}



// 총 결제 금액 나타내는 함수
function updateTotalPrice(){
    var productPrice = document.getElementById("productPrice").innerText.replace(/,/g,'');
    var countSelect = document.getElementById("countSelect").value;
    var deliveryPay = parseInt(document.getElementById("deliveryPay").value);
    var totalPrice = productPrice * countSelect;
    var totalPayment = totalPrice + deliveryPay;
    document.getElementById("totalPrice").innerText = totalPrice.toLocaleString()+"원";
    document.getElementById("totalPayment").innerText = totalPayment.toLocaleString()+"원";
}

//https://hstory0208.tistory.com/entry/Spring-%EC%95%84%EC%9E%84%ED%8F%AC%ED%8A%B8import%EB%A1%9C-%EA%B2%B0%EC%A0%9C-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0-%ED%81%B4%EB%9D%BC%EC%9D%B4%EC%96%B8%ED%8A%B8-%EC%84%9C%EB%B2%84-%EC%BD%94%EB%93%9C-%ED%8F%AC%ED%95%A8
//https://velog.io/@dev_h_o/Spring-%ED%8F%AC%ED%8A%B8%EC%9B%90%EC%95%84%EC%9E%84%ED%8F%AC%ED%8A%B8%EA%B2%B0%EC%A0%9Capi-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0-ajax

//결제하기 버튼 누르면 portOne api 작동
document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("paymentBtn").addEventListener("click", function(event) {
        event.preventDefault(); // 기본 동작 방지
        if(paymentValidChk(event)) {
             goToPay();
        }
    });
});

function goToPay(){

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
    var proCode = document.getElementById("proCode").value;

    IMP.request_pay({
        pg: "html5_inicis.INIpayTest",
        pay_method: payMethod,
        merchant_uid: merchantUid,
        name: productName,
        amount: amount,
        buyer_tel: buyerTel,
        buyer_name: buyerName,
        buyer_addr: buyerAddr,
        buyer_postcode: buyerPostcode

},function(resp){
        if(resp.success){
           var msg='결제 성공했습니다'

           console.log(resp);
           // form 전 여기에서 값 넣기

           var result={
               "orderMethod" : resp.pay_method+" "+resp.card_name,
               "orderDate" : new Date().toISOString().slice(0,10),
               "totalAmount" : amount,
               "impUid" : resp.imp_uid,
               "merchantUid" : merchantUid,
               "buyerName": buyerName,
               "buyerAddr": buyerAddr,
               "buyerTel": buyerTel,
               "buyerPostcode": buyerPostcode,
               "orderCnt" : orderCnt,
               "proCode" : proCode
           }
           console.log(result);

           $.ajax({
                url: '/th/payment/complete',
                method: 'POST',
                data: result,
                success: function (resp) {
//                    console.log("success..."+resp);
                    alert("결제 내역 페이지로 넘어갑니다.");
                    window.location.href = '/th/payment/paymentHistory';
                },
                error: function (err) {
                    // 가맹점 서버 결제 API 실패시 로직
                    alert("결제 처리 중 문제가 발생하였습니다.");
                    console.log("errorDetail : " ,JSON.stringify(err,null,2)); // 에러 출력
                }
            });
          }else{
            var msg ='결제 실패하였습니다.';
            msg += '\n에러내용 : ' + resp.error_msg;
          }
          alert(msg);
      });
}


