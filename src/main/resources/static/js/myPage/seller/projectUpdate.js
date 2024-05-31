/* 이미지 유효성 검사 함수 */
function imageValidation(obj) {
    const fileTypes = ['image/gif', 'image/jpeg', 'image/png', 'image/bmp', 'image/tif'];
    if (obj.size > (10 * 1024 * 1024)) { // 10MB 이상인 경우
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
    var maxFileCnt = 1;
    var reader = new FileReader();
    var imageInput = event.target;
    
    // 유효성 검사
    if (!imageValidation(imageInput.files[0])) {
        imageInput.value = ""; // 파일 선택 취소
        return; // 함수 종료
    }

    reader.onload = function(event) {
        // 이전에 추가된 이미지를 모두 삭제
        var imageContainer = document.querySelector("#image_container");
        imageContainer.innerHTML = "";

        var img = document.createElement("img");
        img.setAttribute("src", event.target.result);
        imageContainer.appendChild(img);
    };

    reader.readAsDataURL(imageInput.files[0]);
}




  var fileNo = 0;
  var filesArr = [];
  
  /* 첨부파일 추가 */
  function addFile(obj) {
      var maxFileCnt = 5;   // 첨부파일 최대 개수
      var attFileCnt = document.querySelectorAll('.filebox').length;    // 기존 추가된 첨부파일 개수
      var remainFileCnt = maxFileCnt - attFileCnt;    // 추가로 첨부가능한 개수
      var curFileCnt = obj.files.length;  // 현재 선택된 첨부파일 개수
  
      // 첨부파일 개수 확인
      if (curFileCnt > remainFileCnt) {
          alert("첨부파일은 최대 " + maxFileCnt + "개 까지 첨부 가능합니다.");
          return; // 함수 종료
      }
  
      for (var i = 0; i < Math.min(curFileCnt, remainFileCnt); i++) {
          const file = obj.files[i];
  
          // 첨부파일 검증
          if (validation(file)) {
              // 파일 배열에 담기
              filesArr.push(file);
  
              // 목록 추가
              let fileBox = document.createElement('div');
              fileBox.className = 'filebox';
              fileBox.id = 'file' + fileNo;
  
              let fileName = document.createElement('p');
              fileName.className = 'name';
              fileName.textContent = file.name;
  
              let deleteBtn = document.createElement('a');
              deleteBtn.className = 'delete';
              deleteBtn.innerHTML = '<i class="far fa-minus-square"></i>';
              deleteBtn.onclick = function () {
                  deleteFile(fileNo);
              };
  
              fileBox.appendChild(fileName);
              fileBox.appendChild(deleteBtn);
  
              document.querySelector('.project-image-subfile-list').appendChild(fileBox);
  
              fileNo++;
          }
      }
      // 초기화
      obj.value = "";
  }
  
  /* 첨부파일 검증 */
  function validation(obj) {
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
      filesArr[num].is_delete = true;
  }


  function validateForm() {
    // 카테고리 선택 확인
    var category = document.querySelector('select[name="language"]').value;
    if (category === "none") {
        alert("프로젝트 카테고리를 선택해주세요.");
        return false;
    }

    // 프로젝트 이름 확인
    var projectName = document.querySelector('input[placeholder="프로젝트 이름을 입력하세요."]').value.trim();
    if (projectName === "") {
        alert("프로젝트 이름을 입력하세요.");
        return false;
    }

    // 프로젝트 가격 확인
    var projectPrice = document.querySelector('input[placeholder="프로젝트 가격을 설정해주세요."]').value.trim();
    if (projectPrice === "") {
        alert("프로젝트 가격을 입력하세요.");
        return false;
    }

    // 상세 설명 확인
    var projectDescription = document.querySelector('.pro-input-text').value.trim();
    if (projectDescription === "") {
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
});

// 숫자만 입력되도록 유효성 검사 함수
function validatePrice(event) {
    var keyCode = event.keyCode;
    // 숫자 키의 keyCode는 48부터 57까지입니다.
    if (keyCode < 48 || keyCode > 57) {
        event.preventDefault(); // 입력한 키가 숫자가 아니면 입력 방지
    }
}

// 프로젝트 가격 입력 필드에 이벤트 리스너 추가
document.querySelector('input[placeholder="프로젝트 가격을 설정해주세요."]').addEventListener('keypress', validatePrice);


// 프로젝트 가격에 쉼표 삽입하는 함수
function addCommasToPrice(price) {
    // 숫자를 문자열로 변환 후, 쉼표 삽입
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

// 프로젝트 가격 입력 필드의 값이 변경될 때마다 호출되는 함수
function formatPriceInput() {
    var projectPriceInput = document.querySelector('input[placeholder="프로젝트 가격을 설정해주세요."]');
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
document.querySelector('input[placeholder="프로젝트 가격을 설정해주세요."]').addEventListener('input', formatPriceInput);


// 프로젝트 상세설명 입력 필드 길이 제한 함수
function limitDescriptionLength(event) {
    var maxLength = 1500; // 최대 길이 설정
    var descriptionInput = document.querySelector('.pro-input-text');
    if (descriptionInput.value.length >= maxLength) {
        // 입력 길이가 최대 길이 이상이면 입력 방지
        event.preventDefault();
    }
}

// 프로젝트 상세설명 입력 필드에 이벤트 리스너 추가
document.querySelector('.pro-input-text').addEventListener('input', limitDescriptionLength);

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

// apply 버튼을 클릭했을 때 trigger되는/ 발동되는 코드안에서 시작/종료 날짜를 받기
$('input[name="datetimes"]').on('apply.daterangepicker', function(ev, picker) {
    let startDate = picker.startDate.format('YYYY-MM-DD');
    let endDate = picker.endDate.format('YYYY-MM-DD');
    console.log("StartDate : " + startDate);    // console 창에서 확인 가능
    console.log("EndDate : " + endDate);

    // javascript -> html 값 전달
    // 참고자료: https://martinnoh.tistory.com/184
    document.proCreateForm.proStartDate.value = new Date(proStartDate);
    document.proCreateForm.proEndDate.value = new Date(proEndDate);
});