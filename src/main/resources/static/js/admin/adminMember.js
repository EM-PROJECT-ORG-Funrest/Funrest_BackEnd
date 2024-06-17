// 회원관리 테이블의 회원 번호를 자동 증가시키기
const rows = document.querySelectorAll('#myTable tbody tr');
let count = 1;

rows.forEach(row => {
    const rowNumberCell = row.querySelector('.row-number');
    rowNumberCell.textContent = count;
    count++;
});