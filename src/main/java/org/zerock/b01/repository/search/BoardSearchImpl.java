package org.zerock.b01.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.QBoard;
import org.zerock.b01.domain.QReply;
import org.zerock.b01.dto.BoardImageDTO;
import org.zerock.b01.dto.BoardListAllDTO;
import org.zerock.b01.dto.BoardListReplyCountDTO;

import java.util.List;
import java.util.stream.Collectors;

// 구현 클래스는 반드시 '인터페이스 이름 + Impl'로 작성해야 한다. (파일 이름이 틀린 경우 제대로 동작하지 않는다.)
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl(){
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable){
        /*
            Querydsl의 목적은 '타입' 기반으로 '코드'를 이용해서 JPQL 쿼리를 생성하고 실행하는 것이다.
                이때 코드를 만드는 대신 클래스가 Q도메인 클래스이다.

            JPQLQuery는 @Query로 작성했던 JPQL을 코드를 통해서 생성할 수 있게 한다.
                이를 통해서 where나 group by 혹은 join 처리 등이 가능하다.

            JPQLQuery의 실행은 fetch()라는 기능을 이용하고, fetchCount()를 이용하면 count 쿼리를 실행할 수 있다.
        */
        QBoard board = QBoard.board;    // Q도메인 객체

        JPQLQuery<Board> query = from(board);   // select .. from board

        // Querydls로 검색 조건과 목록 처리
        BooleanBuilder booleanBuilder = new BooleanBuilder();   // (
        booleanBuilder.or(board.title.contains("11"));          // title like ...
        booleanBuilder.or(board.content.contains("11"));        // content like ...

        query.where(booleanBuilder);
        query.where(board.bno.gt(0L));
        // end

        query.where(board.title.contains("1")); // where title like ...

        //paging
        // Querydsl의 실행시에 Pageable을 처리하는 방법은 QuerydslRepositorySupport 클래스의 기능을 이용한다.
        this.getQuerydsl().applyPagination(pageable, query);    // getQuerydsl() 과 applyPagination()을 이용
                                                                    // TestCase 돌리면 'limit'이 적용된 것을 확인
        List<Board> list = query.fetch();

        long count = query.fetchCount();

        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        if((types != null && types.length > 0) && keyword != null){ // 검색 조건과 키워드가 있다면
            BooleanBuilder booleanBuilder = new BooleanBuilder();   // (
            for(String type : types){
                switch (type){
                    case "t" -> booleanBuilder.or(board.title.contains(keyword));
                    case "c" -> booleanBuilder.or(board.content.contains(keyword));
                    case "w" -> booleanBuilder.or(board.writer.contains(keyword));
                }
            }// end for-each
            query.where(booleanBuilder);
        }// end if

        //bno > 0
        query.where(board.bno.gt(0L));

        //paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();

        long count = query.fetchCount();

        /*
            페이징 처리의 최종 결과는 Page<T>타입을 반환하는 것이므로 Querydsl에서는 이를 직접 처리해야한다.
                Spring Data JPA에서는 이를 처리하기 위해 PageImpl 클래스를 제공하여 3개의 파라미터로 Page<T>를 생성할 수 있다.
                    - List<T> : 실제 목록 데이터
                    - Pageable : 페이지 관련 정보를 가진 객체
                    - long : 전체 개수
         */
        return new PageImpl<>(list, pageable, count);
    }


    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board);
        query.leftJoin(reply).on(reply.board.eq(board));

        query.groupBy(board);

        if((types != null && types.length > 0) && keyword != null){
            BooleanBuilder booleanBuilder = new BooleanBuilder();   // (
            for (String type: types){
                switch (type){
                    case "t" -> booleanBuilder.or(board.title.contains(keyword));
                    case "c" -> booleanBuilder.or(board.content.contains(keyword));
                    case "w" -> booleanBuilder.or(board.writer.contains(keyword));
                }
            }// end for-each
            query.where(booleanBuilder);
        }

        // bno > 0
        query.where(board.bno.gt(0L));

        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections
                .bean(BoardListReplyCountDTO.class,
                        board.bno,
                        board.title,
                        board.writer,
                        board.regDate,
                        reply.count().as("replyCount")
                ));

        this.getQuerydsl().applyPagination(pageable, dtoQuery);

        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();

        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }


    //N+1 문제 - Board와 Reply를 left join 처리
//    @Override
//    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {
//        QBoard board = QBoard.board;
//        QReply reply = QReply.reply;
//
//        JPQLQuery<Board> boardJPQLQuery = from(board);
//        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));   //left join
//
//        getQuerydsl().applyPagination(pageable, boardJPQLQuery);    //paging
//
//        List<Board> boardList = boardJPQLQuery.fetch();
//
//        boardList.forEach(board1 -> {
//            System.out.println(board1.getBno());
//            System.out.println(board1.getImageSet());
//            System.out.println("━━━━━━━━━━━━━━━━━━━━");
//        });
//
//        return null;
//    }


    //게시글의 이미지와 댓글의 숫자까지 처리
    @Override
    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> boardJPQLQuery = from(board);
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));   //left join

        if((types != null && types.length > 0) && keyword != null){
            BooleanBuilder booleanBuilder = new BooleanBuilder();   // (

            for (String type : types){
                switch (type){
                    case "t" -> booleanBuilder.or(board.title.contains(keyword));
                    case "c" -> booleanBuilder.or(board.content.contains(keyword));
                    case "w" -> booleanBuilder.or(board.writer.contains(keyword));
                }
            } // end for-each

            boardJPQLQuery.where(booleanBuilder);
        }

        boardJPQLQuery.groupBy(board);

        getQuerydsl().applyPagination(pageable, boardJPQLQuery);    //paging

        //Tuple이란?
        //셀 수 있는 수량의 순서가 있는 열거 또는 어떤 순서를 따르는 요소들의 모음
        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board, reply.countDistinct());
        List<Tuple> tupleList = tupleJPQLQuery.fetch();

        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {
            Board board1 = (Board) tuple.get(board);
            long replyCount = tuple.get(1, Long.class);

            BoardListAllDTO dto = BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .writer(board1.getWriter())
                    .regDate(board1.getRegDate())
                    .replyCount(replyCount)
                    .build();

            //BoardImage를 BoardImageDTO 처리할 부분
            List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted()
                    .map(boardImage -> BoardImageDTO.builder()
                                    .uuid(boardImage.getUuid())
                                    .fileName(boardImage.getFileName())
                                    .ord(boardImage.getOrd())
                                    .build()
                    ).collect(Collectors.toList());

            //처리된 BoardImageDTO들을 추가
            dto.setBoardImages(imageDTOS);

            return dto;
        }).collect(Collectors.toList());

        long totalCount = boardJPQLQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, totalCount);
    }
}
