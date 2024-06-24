document.addEventListener('DOMContentLoaded', function () {
    function validateForm() {
        // 환불 사유 필드 확인
        var refundDetail = document.querySelector('select[name="refundDetail"]').value;
        if (refundDetail === '') {
            alert("환불 사유를 선택해 주세요.");
            return false;
        }
        // 모든 유효성 검사 통과 시
        alert("환불 신청이 완료되었습니다.");
        return true;
    }

    // 폼 제출 전에 유효성 검사 실행
    document.querySelector('form').addEventListener('submit', function(event) {
        if (!validateForm()) {
            event.preventDefault(); // 제출 이벤트 중지
        }
    });
});

// 결제 취소 요청
function cancelPay() {

    var impUid = document.getElementById("impUid").value;
    var amount = document.getElementById("amount").textContent;
    var reason = document.getElementById("reason").value;
    console.log("impUid", impUid);
    console.log("amount", amount);
    console.log("reason", reason);

    $.ajax({
        "url": "/th/payment/refund", // 예: http://www.myservice.com/payments/cancel
        "type": "POST",
        "contentType": "application/json",
        "data": JSON.stringify({
            "imp_uid": impUid, // 예: ORD20180131-0000011
            "amount": amount, // 환불금액
            "reason": reason // 환불사유
      }),
      "dataType": "json"
    }).done(function(result) { // 환불 성공시 로직
        console.log("result", result);
       giolert("환불 성공");
        window.location.href = '/th/payment/paymentHistory';
    }).fail(function(error) { // 환불 실패시 로직
        console.log("error", error);
        alert("환불 실패");

    });
}
