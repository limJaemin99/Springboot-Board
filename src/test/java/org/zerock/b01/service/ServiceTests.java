package org.zerock.b01.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;

@Log4j2
@SpringBootTest
public class ServiceTests {

    @Autowired
    private BoardService boardService;


    // 등록 테스트
    @Test
    public void registerTest(){

        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample Title ...")
                .content("Sample Content ...")
                .writer("user00")
                .build();

        Long bno = boardService.register(boardDTO);

        log.info("bno : "+bno);
    }


    // 수정 테스트
    @Test
    public void modifyTest(){
        //변경에 필요한 데이터만
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("Update ... 101")
                .content("Update Content 101 ...")
                .build();

        boardService.modify(boardDTO);
    }


    //목록 및 검색 기능 테스트
    @Test
    public void listTest(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);
    }

}
