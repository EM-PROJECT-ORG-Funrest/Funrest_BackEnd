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

    const patternId = /^[a-zA-z0-9]{4,12}$/;

    if(id.value === ""){
        id.classList.add("is-invalid");
        return false;
    } else {
        if(patternId.test(id.value) === false) {
            id.classList.add("is-invalid");
            document.getElementById("validationMessageID").textContent = "영문 대소문자와 숫자를 이용하여 4~12자리로 입력해 주세요.";
            return false;    
        }

        id.classList.remove("is-invalid");
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
            return true;
        }
    }
}