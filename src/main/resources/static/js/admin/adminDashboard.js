document.addEventListener('DOMContentLoaded', () => {
const ctx = document.getElementById('myChart').getContext('2d');

fetch('/api/visitData')
    .then(response => response.json())
    .then(data => {
        // 최근 30개의 데이터만 표시
        const recentData = data.slice(-30);
        const labels = recentData.map(item => item.visitDate);
        const values = recentData.map(item => item.visitNum);

        new Chart(ctx, {
            type: 'line',               // 차트의 종류
            data: {                     // 차트의 데이터
                labels: labels,
                datasets: [{            // 실제 차트에 표시할 데이터들(Array), dataset 객체들을 담고 있음
                    label: '일자별 방문자 수',          // dataset의 이름(String)
                    data: values,                       // dataset의 값(Array)
                    borderColor: 'rgba(75, 192, 192, 1)',       // dataset의 선 색(rgba값을 String으로 표현)
                    borderWidth: 1      // dataset의 선 두께(Number)
                }]
            },
            options: {                              // 차트의 설정
                maintainAspectRatio: false,         // 크기 고정
                scales: {                           // x축과 y축에 대한 설정
                    y: {
                        beginAtZero: true           // 시작을 0부터 하게끔 설정
                    }
                }
            }
        });
    });
});