
// 참고자료: https://soyeon-kim.tistory.com/28
// 로그인 유효성 검사
function submit_check() {

    var id = document.getElementById("id");
    var pw = document.getElementById("pw");

    if (id.value == "") {
        alert("아이디를 입력하세요");
        id.focus;
        return false;
    } else if (pw.value == "") {
        alert("비밀번호를 입력하세요");
        pw.focus();
        return false;
    } else {
        submit();
    }
}

