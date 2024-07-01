 document.addEventListener("DOMContentLoaded", function() {
        CalculateOrderCntAndProPrice();
        CalculateOrderCntAndProPrice2();
    });

function CalculateOrderCntAndProPrice() {
    var proPrice = document.getElementById("proPrice").value;
    var orderCntText = document.getElementById("orderCnt").innerText;
    var orderCnt = parseInt(orderCntText)
    var semiTotalPrice = proPrice * orderCnt;

    console.log("proPrice : ",proPrice);
    console.log("orderCnt : ",orderCnt);
    console.log("semiTotalPrice : ",semiTotalPrice);


    document.getElementById("semiTotalPrice").innerText = semiTotalPrice.toLocaleString()+"원";
}



function CalculateOrderCntAndProPrice2() {
    var proPrice = document.getElementById("proPrice").value;
    var orderCntText = document.getElementById("orderCnt").innerText;
    var orderCnt = parseInt(orderCntText)
    var orderTotalPrice = proPrice * orderCnt;

    document.getElementById("orderTotalPrice").innerText = orderTotalPrice.toLocaleString()+"원";
}

