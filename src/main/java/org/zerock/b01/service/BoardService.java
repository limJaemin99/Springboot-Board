package org.zerock.b01.service;

import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;

public interface BoardService {

    //등록
    Long register(BoardDTO boardDTO);

    //조회
    BoardDTO readOne(Long bno);

    //수정
    void modify(BoardDTO boardDTO);

    //삭제
    void remove(Long bno);

    //목록 및 검색 기능
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

}
