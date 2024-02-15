/*
    Axios를 이용할 때 [async],[await]를 같이 이용하면 비동기 처리를 동기화된 코드처럼 작성할 수 있다.
        ● async : 함수 선언시 사용
            - 해당 함수가 비동기 처리를 위한 함수라는 것을 명시하기 위해 사용
        ● await : async 함수 내에서 비동기 호출하는 부분에 사용
 */


//Axios 동작 확인
async function get1(bno) {
    const result = await axios.get(`/replies/list/${bno}`)
    //① Axios 동작 여부를 Console창에서 확인하기
    //console.log(result) // Console창에서 Object ▶ data ▶ dtoList 에서 댓글 목록 확인 가능

    //② 화면에서 결과가 필요한 경우
    //return result.data

    /*
        비동기 함수이므로 get1()을 호출한 시점에서는 반환할 것이 없지만
            나중에 무언가를 반환할 것이므로 [반환하리고 한 '약속'만을 반환한다.]

        만약 비동기 처리되는 결과를 반환해서 처리한다면 [then()] 과 [catch()] 등을 이용해서 작성한다.
            * reply.js 파일에서는 결과를 반환하도록 구성하고, read.html 파일에서는 then()과 catch()를 이용한다.
     */

    return result
}

/*  ● 비동기 처리 방식의 결정
    비동기 처리할 때는 위와 같이 일반적인 함수와 동작 방식이 다르므로 어떤 방식으로 일관성 있게 처리할 것인지 결정해야 한다.
        비동기 함수를 이용해서 결과 데이터를 처리하는 방식은 크게 다음과 같다.
            - 비동기 함수에서는 순수하게 비동기 통신만 처리하고, 호출한 쪽에서 then()이나 catch()등을 이용해서 처리하는 방식
            - 비동기 함수를 호출할 때, 나중에 처리해야 하는 내용을 같이 별도의 함수로 구성해서 파라미터로 전송하는 방식
            * get1() 함수는 reply.js가 비동기 통신을 담당하고, 화면은 read.html에서 처리하도록 했다.
                reply.js는 Axios를 이용해서 Ajax 통신하는 부분이므로, 코드의 양이 많지는 않지만,
                통신하는 영역과 이벤트나 화면 처리 영역을 분리하기 위해서 사용한다.
 */


//댓글 목록 처리
async function getList({bno, page, size, goLast}) {
    /*  ● 댓글 목록 처리
        [파라미터]
            - bno : 현재 게시물 번호
            - page : 페이지 번호
            - size : 페이지당 사이즈
            - goLast : 마지막 페이지 호출 여부
                * goLast는 페이징 처리시 새로 등록된 댓글이 마지막 페이지에 있는 문제를 보완하기 위함
     */
    const result = await axios.get(`/replies/list/${bno}`, {params: {page, size}})

    //마지막 페이지로 호출할 수 있는 goLast 변수 추가
    if(goLast){
        const total = result.data.total
        const lastPage = parseInt(Math.ceil(total/size))

        return getList({bno:bno, page:lastPage, size:size})
    }

    return result.data
}


//댓글 등록
async function addReply(replyObj) {
    const response = await axios.post(`/replies/`, replyObj)

    return response.data
}


//댓글 조회 (GET)
async function getReply(rno) {
    const response = await axios.get(`/replies/${rno}`)

    return response.data
}


//댓글 수정 (PUT)
async function modifyReply(replyObj) {
    const response = await axios.put(`/replies/${replyObj.rno}`, replyObj)

    return response.data
}


//댓글 삭제 (DELETE)
async function removeReply(rno) {
    const response = await axios.delete(`/replies/${rno}`)

    return response.data
}