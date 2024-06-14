
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
}


//document.write('<script src="https://cdn.iamport.kr/v1/iamport.js"></script>');

//jQuery(document).ready(function(){
//   $('#paymentBtn').on('click',function(event){
//    event.preventDefault(); //기본 동작 방지
//    goToPay();
//   });
//});
document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("paymentBtn").addEventListener("click", function(event) {
        event.preventDefault(); // 기본 동작 방지
        goToPay();
    });
});

function goToPay(){

    IMP.init("imp83318333");

    var payMethod = document.querySelector(".form-check-input").value;
        var merchantUid = document.getElementById("payment-name").value + new Date().getTime();
        var productName = "상품정보(이름)";
        var amount = document.querySelector(".count-select").value;
        var buyerTel = document.getElementById("payment-phone").value;
        var buyerName = document.getElementById("payment-name").value;
        var buyerAddr = document.getElementById("payment-road-addr").value + document.getElementById("payment-addr-detail").value;
        var buyerPostcode = document.getElementById("payment-post-code").value;

        IMP.request_pay({
//            pg: "html5_inicis.INIpayTest",
//            pay_method: $(".form-check-input").val(),
//            merchant_uid: $("#payment-name").val()+ new Date().getTime(),
//            name: "상품정보(이름)",
//            amount: $(".count-select").val(),
//            buyer_tel: $("#payment-phone").val(),
//            buyer_name : $("#payment-name").val(),
//            buyer_addr : $("#payment-road-addr").val()+$("#payment-addr-detail").val(),
//            buyer_postcode : $("#payment-post-code").val()
            //나중에 리다이렉션 주소 필요하면 추가하기 redirect_url : "";

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
                alert("결제 성공했습니다.");
                console.log(resp);
            }else{
                alert("결제 실패하였습니다.");
                console.log(resp)
            }
          }
        );

}

