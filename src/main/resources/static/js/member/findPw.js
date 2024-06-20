async function findPwValidChk(event) {
    event.preventDefault();

    const email = document.getElementById("findPw-email");
    const pattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;

    if(email.value === "") {
        email.classList.add("is-invalid");
        console.log("Email is empty");
        return false;
    } else if(pattern.test(email.value) === false){
         email.classList.add("is-invalid");
        console.log("Email pattern is invalid");
        return false;
    } else {
          email.classList.remove("is-invalid");
          try {
               const response = await axios.post('/th/member/findPwCheck', null, {
                    params: { userId: email.value }
               });
               alert(response.data.message);
          } catch (error) {
                if (error.response) {
                   alert(error.response.data.message);
               } else {
                   console.error('Error:', error);
               }
          }
    }
}