package org.zerock.b01.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.BoardListAllDTO;
import org.zerock.b01.dto.BoardListReplyCountDTO;

public interface BoardSearch {
    /*  Querydsl을 기존 코드에 연동하는 방법
        ① Querydsl을 이용할 인터페이스 선언
        ② '인터페이스 이름 + Impl'이라는 이름으로 클래스를 선언
            - 이때 QuerydslRepositorySupport라는 부모 클래스를 지정하고 인터페이스를 구현
        ③ 기존의 Repository에는 부모 인터페이스로 Querydsl을 위한 인터페이스를 지정
    */
    // 단순 페이지 처리 기능
    Page<Board> search1(Pageable pageable);

    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    //댓글 목록 처리 Querydsl
    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable);

    //N+1 문제 - Board와 Reply를 left join 처리
    //Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable);

    //게시글의 이미지와 댓글의 숫자까지 처리
    Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable);
}
