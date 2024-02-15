package org.zerock.b01.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b01.dto.ReplyDTO;

@SpringBootTest
@Log4j2
public class ReplyServiceTests {

    @Autowired
    private ReplyService replyService;

    //댓글 등록 테스트
    @Test
    public void registerTest(){
        ReplyDTO replyDTO = ReplyDTO.builder()
                .replyText("ReplyDTO Text")
                .replyer("replyer")
                .bno(410L)
                .build();

        log.info(replyService.register(replyDTO));
    }

}
