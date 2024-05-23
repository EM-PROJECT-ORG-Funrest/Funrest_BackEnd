// 페이지 로드 시 스토리를 접은 상태로 설정
window.onload = function() {
    pro_story_hidden_content.style.display = 'none';
    pro_story_hidden_button.innerText = "스토리 더보기";
};

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