package org.zerock.b01.dto.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadResultDTO {

    private String uuid;

    private String fileName;

    private boolean img;

    //업로드된 파일의 UUID값과 파일 이름, 이미지 여부를 객체로 구성하고 getLink()를 통해 첨부파일 경로 처리에 사용
        // * 나중에 JSON으로 처리될 때는 link라는 속성으로 자동 처리된다.
    private String getLink(){
        if(img){
            //이미지인 경우 썸네일
            return "s_" + uuid + "_" + fileName;
        } else {
            return uuid + "_" + fileName;
        }
    }

}
