// 참고자료: https://java119.tistory.com/71
// 변경할 비밀번호 유효성 검사
// 참고자료: https://pycoding.tistory.com/entry/%EC%9E%90%EB%B0%94%EC%8A%A4%ED%81%AC%EB%A6%BD%ED%8A%B8-%EB%B9%84%EB%B0%80%EB%B2%88%ED%98%B8-%EC%9D%BC%EC%B9%98-%ED%99%95%EC%9D%B8
// 변경할 비밀번호(password1)와 비밀번호 확인(password2)의 동일 여부 테스트 검사
function editPassword(event){


    const p2 = document.getElementById('password2');
    const p3 = document.getElementById('password3');

    const num = p2.value.search(/[0-9]/g);
    const eng = p2.value.search(/[a-z]/ig);
    const spe = p2.value.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

    // 기존 비밀번호와 비교(검사)하는 로직 추가 필요

    if(p2.value === "" || p3.value === ""){
        alert("항목을 입력해 주세요.");
        event.preventDefault();
        return false;
    } else if(p2.value.length < 8 || p2.value.length > 20) {
        alert("8자리 ~ 20자리 이내로 입력해주세요.");
        p2.classList.add("is-invalid");
        event.preventDefault();
        return false;
    } else if(p2.value.search(/\s/) != -1) {
        alert("비밀번호는 공백 없이 입력해주세요.");
        p2.classList.add("is-invalid");
        event.preventDefault();
        return false;
    } else if(num < 0 || eng < 0 || spe < 0 ) {
        alert("영문,숫자, 특수문자를 혼합하여 입력해주세요.");
        p2.classList.add("is-invalid");
        event.preventDefault();
        return false;
    } else if(p2.value != p3.value) {
        alert("입력한 새 비밀번호와 일치하지 않습니다.");
        p2.classList.remove("is-invalid");
        p3.classList.add("is-invalid");
        event.preventDefault();
        return false;
    } else {
        p3.classList.remove("is-invalid");
        return true;
    }
}