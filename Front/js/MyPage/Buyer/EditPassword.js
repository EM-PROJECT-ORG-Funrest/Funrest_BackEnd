// 참고자료: https://java119.tistory.com/71
// 변경할 비밀번호 유효성 검사
function chkPW(){

    var pw = $("#password1").val();
    var num = pw.search(/[0-9]/g);
    var eng = pw.search(/[a-z]/ig);
    var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);
   
    if(pw.length < 8 || pw.length > 20) {
        alert("8자리 ~ 20자리 이내로 입력해주세요.");
        return false;
    } else if(pw.search(/\s/) != -1) {
        alert("비밀번호는 공백 없이 입력해주세요.");
        return false;
    } else if(num < 0 || eng < 0 || spe < 0 ) {
        alert("영문,숫자, 특수문자를 혼합하여 입력해주세요.");
        return false;
    } else {
        alert("통과");
        console.log("통과"); 
        return true;
    }
   
}

// 참고자료: https://pycoding.tistory.com/entry/%EC%9E%90%EB%B0%94%EC%8A%A4%ED%81%AC%EB%A6%BD%ED%8A%B8-%EB%B9%84%EB%B0%80%EB%B2%88%ED%98%B8-%EC%9D%BC%EC%B9%98-%ED%99%95%EC%9D%B8
// 변경할 비밀번호(password1)와 비밀번호 확인(password2)의 동일 여부 테스트 검사
function test(){

    var p1 = document.getElementById('password1').value;
    var p2 = document.getElementById('password2').value;

    if(p1 != p2) {
        alert("비밀번호 불일치");
        return false;
    } else {
        alert("비밀번호가 일치합니다");
        return true;
    }
}