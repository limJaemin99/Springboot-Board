//Axios를 통한 파일 업로드

//서버에 첨부파일 업로드
async function uploadToServer(formObj) {
    console.log("upload to server .....")
    console.log(formObj)

    const response = await axios({
        method: 'post',
        url: '/upload',
        data: formObj,
        headers: {
            'Content-Type': 'multipart/form-data',
        },
    });

    return response.data
}

//특정 파일 삭제
async function removeFileToServer(uuid, fileName) {
    const response = await axios.delete(`/remove/${uuid}_${fileName}`)

    return response.data
}