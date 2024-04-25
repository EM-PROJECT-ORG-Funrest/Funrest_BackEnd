function signupValidChk() {
    const elId = document.getElementById("signup-id");
    const elPw = document.getElementById("signup-pw");
    const elChk = document.getElementById("signup-pw-chk")
    const id = elId.value;
    const pw = elPw.value;
    const pwChk = elChk.value;
    const patternId = /^[a-zA-z0-9]{4,12}$/;
    const patternPw = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/;

    if(patternId.test(id) === false) {
        elId.classList.add("is-invalid");
        return false;
    } else {
        elId.classList.remove("is-invalid");

        if(patternPw.test(pw) === false) {
            elPw.classList.add("is-invalid");
            console.log("1");
            return false;

        } else {
            elPw.classList.remove("is-invalid");
            console.log("2");

            if (pw != pwChk) {
                elChk.classList.add("is-invalid");
                return false;
            }

            elChk.classList.remove("is-invalid");
            return true;
        }
    }
}