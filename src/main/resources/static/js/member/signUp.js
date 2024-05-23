function chkId() {
    const id = document.getElementById("signup-id");
    const pattern =  /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;

    if(id.value === ""){
        document.getElementById("validationMessageID").textContent = "유효한 이메일 형식이 아닙니다.";
        id.classList.add("is-invalid");
        return false;
    } else if(pattern.test(id.value) === false) { 
        document.getElementById("validationMessageID").textContent = "유효한 이메일 형식이 아닙니다.";
        id.classList.add("is-invalid");
        return false;
    } else {
        id.classList.remove("is-invalid");
        return true;
    }
}

// 이메일 인증되었는지 확인하는 함수
function isEmailVerified(emailAddr) {
    // 여기에 이메일 인증 여부를 확인하는 로직을 추가
    return true;
}

function chkPW(pwEl){
    var pw = pwEl.value;
    var num = pw.search(/[0-9]/g);
    var eng = pw.search(/[a-z]/ig);
    var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

    if(pw.length < 8 || pw.length > 20){
        document.getElementById("validationMessagePW").textContent = "8자리 ~ 20자리 이내로 입력해주세요.";
        return false;
    }else if(pw.search(/\s/) != -1){
        document.getElementById("validationMessagePW").textContent = "비밀번호는 공백 없이 입력해주세요.";
        return false;
    }else if(num < 0 || eng < 0 || spe < 0 ){
        document.getElementById("validationMessagePW").textContent = "영문,숫자, 특수문자를 혼합하여 입력해주세요.";
        return false;
    }else {
        console.log("통과"); 
        return true;
    }
}

function signupValidChk(event) {
    event.preventDefault();

    const id = document.getElementById("signup-id");
    const pw = document.getElementById("signup-pw");
    const pwChk = document.getElementById("signup-pw-chk");

    // 이메일이 인증되지 않은 경우
    if (!isEmailVerified(id.value)) {
        document.getElementById("validationMessageID").textContent = "이메일 인증을 완료해 주세요.";
        id.classList.add("is-invalid");
        return false;
    }

    if(pw.value === "" || !chkPW(pw)){
        pw.classList.add("is-invalid");
        return false;
    } else {
        pw.classList.remove("is-invalid");
        
        if(pw.value !== pwChk.value) {
            pwChk.classList.add("is-invalid");
            return false;
        } else {
            pwChk.classList.remove("is-invalid");
            return true;
        }
    }
}