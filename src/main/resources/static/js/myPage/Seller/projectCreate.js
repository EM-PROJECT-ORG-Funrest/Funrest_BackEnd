function imageValidation(obj) {
    const fileTypes = ['image/gif', 'image/jpeg', 'image/png', 'image/bmp', 'image/tif'];
    const maxSize = 10 * 1024 * 1024; // 10MB
    if (obj.size > maxSize) { // 10MB 이상인 경우
        alert("이미지 용량은 10MB를 초과할 수 없습니다.");
        return false;
    } else if (!fileTypes.includes(obj.type)) { // 지원되지 않는 파일 형식인 경우
        alert("지원되지 않는 파일 형식입니다. GIF, JPEG, PNG, BMP, TIF 형식의 이미지를 선택하세요.");
        return false;
    } else {
        return true;
    }
}

/* 이미지 파일 선택 시 처리 함수 */
function setThumbnail(event) {
    var maxFileCnt = 3;
    var reader = new FileReader();
    var imageInput = event.target;

    // 파일 개수 제한
    if (imageInput.files.length > maxFileCnt) {
        alert("첨부파일은 최대 " + maxFileCnt + "개 까지 첨부 가능합니다.");
        imageInput.value = ""; // 파일 선택 취소
        return; // 함수 종료
    }

    // 유효성 검사
    if (!imageValidation(imageInput.files[0])) {
        imageInput.value = ""; // 파일 선택 취소
        return; // 함수 종료
    }

    reader.onload = function(event) {

        var imageContainer = document.querySelector("#image_container");
        imageContainer.innerHTML = "";  // 이전에 추가된 이미지를 모두 삭제

        var img = document.createElement("img");
        img.setAttribute("src", event.target.result);

        // 인라인 스타일로 크기 조정
        img.style.width = "100%";
        img.style.height = "100%";

        imageContainer.appendChild(img);
    };

    reader.readAsDataURL(imageInput.files[0]); // 파일을 읽고 그 결과를 데이터 URL(data URL)로 변환
}

var fileNo = 0;

/* 첨부파일 추가 */
function addFile(obj) {
    var maxFileCnt = 5;   // 첨부파일 최대 개수
    var attFileCnt = document.querySelectorAll('.filebox').length;    // 기존 추가된 첨부파일 개수
    var remainFileCnt = maxFileCnt - attFileCnt;    // 추가로 첨부가능한 개수
    var curFileCnt = obj.files.length;  // 현재 선택된 첨부파일 개수

    // 첨부파일 개수 확인
    if (curFileCnt > remainFileCnt) {
        alert("첨부파일은 최대 " + maxFileCnt + "개 까지 첨부 가능합니다.");
    } else {
        for (const file of obj.files) {
            // 첨부파일 검증
            if (validation(file)) {
                // 목록 추가
                let htmlData = '';
                htmlData += '<div id="file' + fileNo + '" class="filebox">';
                htmlData += '   <p class="name">' + file.name + '</p>';
                htmlData += '   <a class="delete" onclick="deleteFile(' + fileNo + ');"><p class="text-center">X</p></a>';
                htmlData += '</div>';
                $('.project-image-subfile-list').append(htmlData);
                fileNo++;
            } else {
                continue;
            }
        }
    }
}

/* 첨부파일 검증 */
function validation(obj){
    const fileTypes = ['application/pdf', 'image/gif', 'image/jpeg', 'image/png', 'image/bmp', 'image/tif', 'application/haansofthwp', 'application/x-hwp'];
    if (obj.name.length > 100) {
        alert("파일명이 100자 이상인 파일은 제외되었습니다.");
        return false;
    } else if (obj.size > (100 * 1024 * 1024)) {
        alert("최대 파일 용량인 100MB를 초과한 파일은 제외되었습니다.");
        return false;
    } else if (obj.name.lastIndexOf('.') == -1) {
        alert("확장자가 없는 파일은 제외되었습니다.");
        return false;
    } else if (!fileTypes.includes(obj.type)) {
        alert("첨부가 불가능한 파일은 제외되었습니다.");
        return false;
    } else {
        return true;
    }
}

/* 첨부파일 삭제 */
function deleteFile(num) {
    document.querySelector("#file" + num).remove();
}

// 모듬 입력 필드 null(빈 값) 체크
document.addEventListener('DOMContentLoaded', function() {
    function validateForm() {
        // 판매자 이름 확인
        var sellerName = document.querySelector('input[name="sellerName"]').value.trim();
        if (sellerName === "") {
            alert("판매자 이름을 입력하세요.");
            return false;
        }
        // 판매자 소개글 확인
        var sellerDetail = document.querySelector('textarea[name="sellerDetail"]').value.trim();
        if (sellerDetail === "") {
            alert("판매자 소개글을 입력하세요.");
            return false;
        }
        // 카테고리 선택 확인
        var category = document.querySelector('select[name="proCategory"]').value;
        if (category === "none") {
            alert("프로젝트 카테고리를 선택해주세요.");
            return false;
        }
        // 프로젝트 이름 확인
        var projectName = document.querySelector('input[name="proName"]').value.trim();
        if (projectName === "") {
            alert("프로젝트 이름을 입력하세요.");
            return false;
        }
        // 프로젝트 가격 확인
        var projectPrice = document.querySelector('input[name="proPrice"]').value.trim();
        if (projectPrice === "") {
            alert("프로젝트 가격을 입력하세요.");
            return false;
        }
        // 프로젝트 목표 금액 확인
        var projectGoalAmount = document.querySelector('input[name="proGoalAmount"]').value.trim();
        if (projectGoalAmount === "") {
            alert("프로젝트 목표 금액을 입력하세요.");
            return false;
        }
        // 프로젝트 상세설명 확인
        var projectScript = document.querySelector('textarea[name="proScript"]').value.trim();
        if (projectScript === "") {
            alert("프로젝트 상세 설명을 입력하세요.");
            return false;
        }
        return true;
    }
    // 폼 제출 전에 유효성 검사 실행
    document.querySelector('form').addEventListener('submit', function(event) {
        if (!validateForm()) {
            event.preventDefault(); // 폼 제출 중지
        }
    })
});

// 프로젝트 가격에 쉼표 삽입하는 함수
function addCommasToPrice(price) {
    // 숫자를 문자열로 변환 후, 쉼표 삽입
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

// 프로젝트 가격 입력 필드의 값이 변경될 때마다 호출되는 함수
function formatPriceInput() {
    var projectPriceInput = document.querySelector('input[name="proPrice"]');
    var currentPrice = projectPriceInput.value.replace(/,/g, ''); // 쉼표 제거한 현재 값
    // 현재 값이 숫자가 아니면 제거하고 리턴
    if (isNaN(currentPrice)) {
        projectPriceInput.value = '';
        return;
    }
    // 숫자를 천 단위로 쉼표로 구분하여 표시
    projectPriceInput.value = addCommasToPrice(currentPrice);
}
// 프로젝트 가격 입력 필드에 이벤트 리스너 추가
document.querySelector('input[name="proPrice"]').addEventListener('input', formatPriceInput);

// 프로젝트 목표 금액 입력 필드의 값이 변경될 때마다 호출되는 함수
function formatGoalAmountInput() {
    var projectGoalAmountInput = document.querySelector('input[name="proGoalAmount"]'); // 프로젝트 목표 금액 입력 필드 변수 선언
    var currentGoalAmount = projectGoalAmountInput.value.replace(/,/g, ''); // 쉼표 제거한 현재 값 변수 선언
    // 현재 값이 숫자가 아니면 제거하고 리턴
    if (isNaN(currentGoalAmount)) {
        projectGoalAmountInput.value = '';
        return;
    }
    // 숫자를 천 단위로 쉼표로 구분하여 표시
    projectGoalAmountInput.value = addCommasToPrice(currentGoalAmount);
}
// 프로젝트 목표 금액 입력 필드에 'formatGoalAmountInput' 이벤트 리스너 추가
document.querySelector('input[name="proGoalAmount"]').addEventListener('input', formatGoalAmountInput);

// 판매자 소개글 입려 필드 길이 제한 함수
function limitSellerDetailLength(event) {
    var maxLength = 300; // 최대 길이 설정
    var sellerDetailInput = document.querySelector('textarea[name="sellerDetail"]'); // 판매자 소개글 입력 필드 변수 선언
    if (sellerDetailInput.value.length >= maxLength) // 판매자 소개글 입력 필드의 값이 최대 길이 이상이면
    {
        sellerDetailInput.value = sellerDetailInput.value.substring(0, maxLength); // 최대 길이까지 잘라내기
    }
}
// 판매자 소개글 입력 필드에 이벤트 리스너 추가
document.querySelector('textarea[name="sellerDetail"]').addEventListener('input', limitSellerDetailLength)

// 프로젝트 상세설명 입력 필드 길이 제한 함수
function limitProScriptLength(event) {
    var maxLength = 300; // 최대 길이 설정
    var proScripInput = document.querySelector('textarea[name="proScript"]'); // 프로젝트 상세설명 입력 필드 변수 선언
    if (proScripInput.value.length >= maxLength) // 프로젝트 상세설명 입력 필드의 값이 최대 길이 이상이면
    {
        // event.preventDefault(); // input 이벤트는 기본 동작을 취소하지 않아 해당 코드 적용 안됨
        proScripInput.value = proScripInput.value.substring(0, maxLength); // 최대 길이까지 잘라내기
    }
}
// 프로젝트 상세설명 입력 필드에 이벤트 리스너 추가
document.querySelector('textarea[name="proScript"]').addEventListener('input', limitProScriptLength);

// DateRangePicker
$(function() {
    $('input[name="datetimes"]').daterangepicker({
        timePicker: false, // 시간 선택 비활성화
        startDate: moment().startOf('day'),
        endDate: moment().startOf('day').add(1, 'day'),
        locale: {
            format: 'YYYY-MM-DD'    // 날짜 포맷 설정
        }
    });
});

// apply 버튼을 클릭했을 때 trigger 되는 코드안에서 시작/종료 날짜를 받기
$('input[name="datetimes"]').on('apply.daterangepicker', function(ev, picker) {
    const proStartDate = picker.startDate.format('YYYY-MM-DD');
    const proEndDate = picker.endDate.format('YYYY-MM-DD');
    // console.log("proStartDate : " + proStartDate);    // console 창에서 확인 가능
    // console.log("proEndDate : " + proEndDate);

    // javascript -> html 값 전달
    // 참고자료: https://martinnoh.tistory.com/184
    document.proCreateForm.proStartDate.value = proStartDate;
    document.proCreateForm.proEndDate.value = proEndDate;
});