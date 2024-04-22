function setThumbnail(event) {
    var maxFileCnt = 1;
    var reader = new FileReader();

    reader.onload = function(event) {
        // 이전에 추가된 이미지를 모두 삭제
        var imageContainer = document.querySelector("#image_container");
        imageContainer.innerHTML = "";

        var img = document.createElement("img");
        img.setAttribute("src", event.target.result);
        imageContainer.appendChild(img);
    };

    reader.readAsDataURL(event.target.files[0]);
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
  
              document.querySelector('.create-project-image-file-list').appendChild(fileBox);
  
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