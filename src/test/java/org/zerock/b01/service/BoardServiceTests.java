package org.zerock.b01.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b01.dto.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Log4j2
@SpringBootTest
public class BoardServiceTests {

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

        //첨부파일을 하나 추가
        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID() + "_zzz.jpg"));

        boardService.modify(boardDTO);
    }


    //삭제 테스트
    @Test
    public void removeAllTest(){
        Long bno = 1L;

        boardService.remove(bno);
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


    //DTO를 엔티티로 변환하여 등록 테스트
    @Test
    public void registerWithImagesTest(){
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("File ... Sample title ...")
                .content("Sample Content .....")
                .writer("user00")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID() + "_aaa.jpg",
                        UUID.randomUUID() + "_bbb.jpg",
                        UUID.randomUUID() + "_ccc.jpg"
                )
        );

        Long bno = boardService.register(boardDTO);

        log.info("bno : "+bno);

    }


    //게시물과 첨부파일의 정보가 한번에 처리되는지 테스트
    @Test
    public void readAllTest(){
        Long bno = 101L;

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        for (String fileName : boardDTO.getFileNames()){
            log.info(fileName);
        }
    }


    //게시물 목록 처리 테스트
    @Test
    public void listWithAllTest(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        System.err.println("메소드 시작");

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);
        System.err.println("메소드 종료");

        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO -> {
            log.info(boardListAllDTO.getBno() + " : " + boardListAllDTO.getTitle());

            if(boardListAllDTO.getBoardImages() != null){
                for (BoardImageDTO boardImage : boardListAllDTO.getBoardImages()){
                    log.info(boardImage);
                }
            }

            log.info("━━━━━━━━━━━━━━━━━━━━");
        });
    }
}
