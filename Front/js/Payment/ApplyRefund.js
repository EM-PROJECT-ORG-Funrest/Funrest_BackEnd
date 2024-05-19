document.addEventListener('DOMContentLoaded', function () {
    const submitButton = document.querySelector('.btn-apply');
    submitButton.addEventListener('click', validateSelectionOnce);
});

function validateSelection(event) {

    event.preventDefault();

    const selectedProduct = document.querySelector('.pay-product-select').value;
    const selectedQuantity = document.querySelector('.count-select').value;
    const selectedReason = document.querySelector('.refund-reason').value;

    if(selectedProduct === "결제 상품 선택"){
        alert("결제 상품을 선택해 주세요.");
        return;
    }

    if(selectedQuantity === "수량 선택"){
        alert("수량을 선택해 주세요.");
        return;
    }
    
    if(selectedReason === "사유 선택"){
        alert("환불 사유를 선택해 주세요.")
        return;
    }

    alert("환불 신청이 완료되었습니다.");
    
    }