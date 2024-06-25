document.addEventListener('DOMContentLoaded', function () {
    document.getElementById("refundBtn").addEventListener("click", function(event) {
        event.preventDefault(); // 기본 제출 동작 중지
        if (validateForm()) {
            cancelPay(); // 유효성 검사 통과 시 결제 취소 함수 실행
        }
    });
});

function validateForm() {
    var reason = document.querySelector('select[name="reason"]').value;
    if (reason === '') {
        alert("환불 사유를 선택해 주세요.");
        return false;
    }
    alert("환불 신청이 완료되었습니다.");
    return true;
}

function cancelPay() {
    var imp_uid = document.getElementById("imp_uid").value;
    var reason = document.getElementById("reason").value;
    var userId = document.getElementById("userId").value;
    var orderState = '환불완료';

    console.log("imp_uid:", imp_uid);
    console.log("reason:", reason);

    $.ajax({
        url: "/api/refund",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            "imp_uid": imp_uid,
            "reason": reason
        }),
    }).done(function(result) {
        var redirectUrl = '/th/payment/refundHistory/' + userId + '?orderState=' + encodeURIComponent(orderState);
        console.log("result", result);
        alert("환불 성공했습니다");
        window.location.href = redirectUrl;
    }).fail(function(error) {
        console.log("error", error);
        alert("환불 실패했습니다");
    });
}