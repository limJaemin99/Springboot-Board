//좌상단 홈버튼
document.querySelector(".homeDiv").addEventListener("click", function (e) {
    e.preventDefault()
    e.stopPropagation()

    location.href = '/';
})

//토글 메뉴 on/off
let status = true;

document.querySelector("#sidebarToggle").addEventListener("click", function (e) {
    e.preventDefault()
    e.stopPropagation()

    const toggleBtn = document.querySelector("#sidebarToggle")

    if(status){
        status = false;
        toggleBtn.innerHTML = '▶ 메뉴 열기'
    } else {
        status = true;
        toggleBtn.innerHTML = '◀ 메뉴 닫기'
    }
})