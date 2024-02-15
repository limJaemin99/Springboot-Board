package org.zerock.b01.service;

import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.ReplyDTO;

public interface ReplyService {

    //댓글 작성
    Long register(ReplyDTO replyDTO);

    //댓글 출력
    ReplyDTO read(Long rno);

    //댓글 수정 (내용만 가능)
    void modify(ReplyDTO replyDTO);

    //댓글 삭제
    void remove(Long rno);

    //특정 게시물의 댓글 목록 처리
    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);

}
