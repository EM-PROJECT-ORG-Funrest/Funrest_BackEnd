// 페이지 로드 시 스토리를 접은 상태로 설정
window.onload = function() {
    pro_story_hidden_content.style.display = 'none';
    pro_story_hidden_button.innerText = "스토리 더보기";
};


const pro_story_hidden_content = document.getElementById('pro-story-hidden-content');

function btnClick() {
    if (pro_story_hidden_content.style.display === 'block') {
        pro_story_hidden_content.style.display = 'none';
    } else {
        pro_story_hidden_content.style.display = 'block';
    }
  };

var pro_story_hidden_button = document.getElementById("pro-story-hidden-button-btn");

pro_story_hidden_button.addEventListener("click", function() {
    if(pro_story_hidden_button.innerText === "스토리 더보기") {
        pro_story_hidden_button.innerText = "스토리 접기";
    } else {
        pro_story_hidden_button.innerText = "스토리 더보기";
    }
});


const header_btn = document.querySelector('.header-btn');

header_btn.onclick = () => {
window.scrollTo({ top: 0, behavior: "smooth" });  
}