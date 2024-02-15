package org.zerock.b01.repository;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.Reply;
import org.zerock.b01.dto.BoardListReplyCountDTO;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    //특정 bno 게시글에 댓글 입력 테스트
    @Test
    public void insertTest(){
        //실제 DB에 존재하는 bno
        Long bno = 411L;

        Board board = Board.builder()
                .bno(bno)
                .build();

        Reply reply = Reply.builder()
                .board(board)
                .replyText("Reply test ...")
                .replyer("replyer")
                .build();

        replyRepository.save(reply);

    }


    //특정 bno의 댓글 전체 출력 테스트
    @Test
    @Transactional  //Reply.java 의 ToString에 exclude 속성이 없을 경우 Transactional을 사용해야 오류가 발생하지 않는다.
    public void boardRepliesTest(){
        Long bno = 411L;

        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());
        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);
        result.getContent().forEach(reply -> {
            log.info(reply);
        });
    }


}
