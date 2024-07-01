// 페이지 로드 시 스토리를 접은 상태로 설정
window.onload = function() {
    pro_story_hidden_content.style.display = 'none';
    pro_story_hidden_button.innerText = "스토리 더보기";
};

// url 복사
function clip(){
	var url = '';
	var textarea = document.createElement("textarea");
	document.body.appendChild(textarea);
	url = window.document.location.href;
	textarea.value = url;
	textarea.select();
	document.execCommand("copy");
	document.body.removeChild(textarea);
	alert("URL이 복사되었습니다.")
}

// btnClick() 함수 정의
// 'pro_story_hidden_content' 요소의 'display'가 'block'(화면에 보이면)이면 'none'으로 변경,
// 'none'(화면에 보이지 않으면)이면 'block'으로 변경한다
const pro_story_hidden_content = document.getElementById('pro-story-hidden-content');

function btnClick() {
    if (pro_story_hidden_content.style.display === 'block') 
    {
        pro_story_hidden_content.style.display = 'none';
    } 
    else 
    {
        pro_story_hidden_content.style.display = 'block';
    }
  };

// 'pro_story_hidden_button' 요소 'click' 이벤트 처리
// 요소의 'innerText'가 '스토리 더보기'면 '스토리 접기'로 변경
// '스토리 접기'이면 '스토리 더보기'로 변경한다
var pro_story_hidden_button = document.getElementById("pro-story-hidden-button-btn");

pro_story_hidden_button.addEventListener("click", function() {
    if (pro_story_hidden_button.innerText === "스토리 더보기") 
    {
        pro_story_hidden_button.innerText = "스토리 접기";
    } 
    else 
    {
        pro_story_hidden_button.innerText = "스토리 더보기";
    }
});

// 'header-btr' 요소 'click' 이벤트 처리
// 'window.scrollTo' 메서드를 호출하여 페이지를 위로 스크롤
const header_btn = document.querySelector('.header-btn');

header_btn.onclick = () => {
window.scrollTo({ top: 0, behavior: "smooth" });  
}

// 알림 신청 버튼 클릭시
const notifyCnt = document.getElementById("notifyCnt");
async function applyNotification(proCode) {
    try {
        const resp = await axios.post("/th/notify/applyNotification", {proCode});
        const resp2 = await axios.get("/th/notify/count/" + proCode);
        if(resp2.data.cnt === undefined) {
            window.alert("로그인이 필요한 기능입니다. 펀레스트 계정이 없다면 회원가입을 진행해주세요!");
            return;
        }

        if(resp2.data) {
            notifyCnt.innerText = resp2.data.cnt;
        }
    } catch(error) {
        console.log(error);
        if(error.response.status === 400) {
            window.alert("이미 알림 신청한 프로젝트입니다.");
            return false;
        } else if(error.response.status === 404) {
            window.alert("찾을 수 없는 프로젝트 또는 유저입니다. 관리자에게 문의해 주세요.");
            return false;
        } else if(error.response.status === 502) {
            window.alert("서버 에러! 관리자에게 문의해 주세요.");
            return false;
        }
    };
}
