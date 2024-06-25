document.addEventListener('DOMContentLoaded', function () {
    function validateForm() {
        // 환불 사유 필드 확인
        var reason = document.querySelector('select[name="reason"]').value;
        if (reason === '') {
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

    var imp_uid = document.getElementById("imp_uid").value;
    var reason = document.getElementById("reason").value;
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
        console.log("result", result);
        alert("환불 성공");
        window.location.href = '/th/payment/paymentHistory';
    }).fail(function(error) {
        console.log("error", error);
        alert("환불 실패");
    });
}
