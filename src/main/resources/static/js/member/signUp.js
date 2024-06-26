let isPassEmailChk = false; //이메일 인증 통과 여부

function chkId() {
    const id = document.getElementById("signup-id");
    const pattern =  /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;
    const idVal = document.getElementById("signup-id-val");
    const idValBtn = document.getElementById("signup-email-val-btn");

    if(id.value === ""){
        id.classList.remove("is-valid");
        idVal.classList.add("d-none");
        idValBtn.classList.add("d-none");
        document.getElementById("validationMessageID").textContent = "유효한 이메일 형식이 아닙니다.";
        id.classList.add("is-invalid");
        return false;
    } else if(pattern.test(id.value) === false) {
        id.classList.remove("is-valid");
        idVal.classList.add("d-none");
        idValBtn.classList.add("d-none");
        document.getElementById("validationMessageID").textContent = "유효한 이메일 형식이 아닙니다.";
        id.classList.add("is-invalid");
        return false;
    } else {
        id.classList.remove("is-invalid");
        id.classList.add("is-valid");
        idVal.classList.remove("d-none");
        idValBtn.classList.remove("d-none");

        axios.get("/th/member/signUp/mail/req/" + id.value)
        .then(resp => {
            console.log(resp);
            return true;
        })
        .catch(error => {
            console.log(error);
            return false;
        });
    }
}

function chkAuth() {
    console.log("clicked");
    const inputCode = document.getElementById("signup-id-val");

    if(inputCode.value === ""){
        document.getElementById("validationMessageIDVal").textContent = "인증번호를 입력해 주세요.";
        inputCode.classList.add("is-invalid");
        return false;
    } else {
        axios.get("/th/member/signUp/mail/verifyCode/" + inputCode.value)
        .then(resp => {
            console.log(resp);
            if(resp.data) {
                inputCode.classList.remove("is-invalid");
                inputCode.classList.add("is-valid");
                isPassEmailChk = true;
                return true;
            } else {
                document.getElementById("validationMessageIDVal").textContent = "인증번호가 맞지 않습니다. 다시 한번 정확히 입력해 주세요.";
                inputCode.classList.add("is-invalid");
                return false;
            }
        })
        .catch(error => {
            console.log(error);
            document.getElementById("validationMessageIDVal").textContent = "메일전송 버튼을 다시 눌러주세요.";
            inputCode.classList.add("is-invalid");
            return false;
        });
    }
}

function chkPW(pwEl){
    const pw = pwEl.value;
    const num = pw.search(/[0-9]/g);
    const eng = pw.search(/[a-z]/ig);
    const spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

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
        console.log("패스워드 통과");
        return true;
    }
}

function signupValidChk(event) {
    event.preventDefault();

     // 폼 데이터 수집
    const form = document.getElementById('signup');
    const formData = new FormData(form);

    const id = document.getElementById("signup-id");
    const pw = document.getElementById("signup-pw");
    const pwChk = document.getElementById("signup-pw-chk");
    const chkBox1 = document.getElementById("agree1");
    const chkBox2 = document.getElementById("agree2");

    // 이메일이 인증되지 않은 경우
    if (!isPassEmailChk) {
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
            //console.log("회원가입 통과");
            pwChk.classList.remove("is-invalid");

            if(!chkBox1.checked || !chkBox2.checked) {
                window.alert("필수 동의 항목을 체크해 주세요.");
                return false;
            }

            axios.post("/th/member/signUp/join", formData)
            .then(resp => {
                if(resp.status === 200) {
                    console.log("회원가입 완료");
                    window.alert("회원가입이 완료 되었습니다.");
                    return location.href="/th/main/main";
                }
            })
            .catch(error => {
                if(error.response) {
                    console.log(error.response.status + " " + error.response.data);
                    if(error.response.status === 400) {
                        window.alert("이미 가입된 이메일 계정입니다.");
                        return false;
                    } else if(error.response.status === 502) {
                        window.alert("서버 에러! 관리자에게 문의해 주세요.");
                        return false;
                    }
                }
                else if (error.request) {
                  // 요청이 만들어졌지만 응답을 받지 못했을 때
                  console.log('No response received:', error.request);
                } else {
                  // 요청을 설정하는 과정에서 에러가 발생했을 때
                  console.log('Error setting up request:', error.message);
                }
            });
        }
    }
}