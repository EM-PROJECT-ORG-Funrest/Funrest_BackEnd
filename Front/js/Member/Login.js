function loginValidChk() {
    const elLoginId = document.getElementById("login-id");
    const elLoginPw = document.getElementById("login-pw");
    const loginid = elLoginId.value;
    const loginpw = elLoginPw.value;

    if (loginid === '') {
        elLoginId.classList.add("is-invalid");
        
        return false;
    } else if (loginpw === '') {
        elLoginId.classList.remove("is-invalid");
        elLoginPw.classList.add("is-invalid");

        return false;
    }

    elLoginPw.classList.remove("is-invalid");
    return true;
}