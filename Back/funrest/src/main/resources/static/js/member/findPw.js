function findPwValidChk(event) {
    event.preventDefault();

    const email = document.getElementById("findpw-email");
    const pattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;

    if(email.value === "") {
        email.classList.add("is-invalid");
        return false;
    } 
        
    if(pattern.test(email.value) === false){
        email.classList.add("is-invalid");
        return false;    
    }
}