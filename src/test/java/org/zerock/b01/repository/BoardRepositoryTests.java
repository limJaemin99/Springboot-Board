package org.zerock.b01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.b01.domain.Board;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    // insert 기능 테스트 - JpaRepository.save()
    @Test
    public void insertTest(){
        /*
            현재의 영속 컨텍스트 내에 데이터가 존재하는지 찾아보고
                해당 엔티티 객체가 없을 경우 insert, 있을 경우 update를 자동으로 실행한다.

            save()의 결과는 DB에 저장된 데이터와 동기화된 Board 객체가 반환된다.
            최종적으로 테스트 실행 후 DB를 조회해보면 100개의 데이터가 생성된 것을 확인할 수 있다.
                (Test 코드의 경우 @Id값이 null이므로 insert만 실행된다.)
         */
        IntStream.rangeClosed(1,100).forEach(i->{
            Board board = Board.builder()
                    .title("title..."+i)
                    .content("content..."+i)
                    .writer("user"+(i%10))
                    .build();

            Board result = boardRepository.save(board);
            log.info("BNO : "+result.getBno());
        });
    }


    // select 기능 테스트 - JpaRepository.findById()
    @Test
    public void selectTest(){
        // findById()의 리턴타입은 Optional<T>이다.
        Long bno = 100L;

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        log.info(board);
    }


    // update 기능 테스트 - JpaRepository.save()
    @Test
    public void updateTest(){
        /*
            update 기능은 insert와 동일하게 save()를 통해 처리된다.
            동일한 @Id값을 가지는 객체를 생성하여 처리할 수 있다.
            update는 등록 시간이 필요하므로 가능하면 findById()를 이용하여 약간의 수정을 통해 처리하는것이 좋다.

            일반적으로 엔티티 객체는 가능한 최소한의 변경이나, 불변(immutable)하게 설계하는 것이 좋지만,
                강제적인 사항은 아니므로 Board 클래스에 수정이 가능한 부분을 미리 메소드로 설계하도록 한다.
                    (Board.change())
         */
        Long bno = 100L;

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        board.change("update..title 100", "update content 100");

        boardRepository.save(board);
    }


    // delete 기능 테스트 - JpaRepository.deleteById()
    @Test
    public void deleteTest(){
        // delete는 @Id에 해당하는 값으로 deleteById()를 통해서 실행할 수 있다.
        Long bno = 1L;

        boardRepository.deleteById(bno);
    }


    // 페이징 테스트 1 - JpaRepository.findAll() , Pageable
    @Test
    public void pagingTest(){
        /*
            ① Pageable 타입의 객체를 구성하여 파라미터로 전달하는 방법
                * Pageable은 인터페이스로 설계되어 있고, 일반적으로는 PageRequest.of()라는 기능을 이용하여 개발이 가능
                    - PageRequest.of(페이지 번호, 사이즈) : 페이지 번호는 0부터
                    - PageRequest.of(페이지 번호, 사이즈, Sort) : 정렬 조건 추가
                    - PageRequest.of(페이지 번호, 사이즈, Sort.Direction, 속성...) : 정렬 방향과 여러 속성 지정
                * 파라미터로 Pageable을 이용하면, 리턴타입은 Page<T>를 이용
                    이는 단순 목록뿐 아니라, 페이징 처리에 데이터가 많은 경우 count 처리를 자동으로 실행한다.
                * 대부분의 Pageable 파라미터는 메소드 마지막에 사용하고
                    파라미터에 Pageable이 있는 경우 메소드의 리턴타입을 Page<T>로 설계한다.

            ② JpaRepository.findAll()을 사용하여 기본적인 페이징 처리를 하는 방법
                * findAll()의 리턴타입으로 나오는 Page<T>타입은 내부적으로 페이징 처리에 필요한 여러 정보를 처리한다.
                    예를 들어, 이전/다음 페이지의 존재 여부, 전체 데이터의 개수는 몇 개 인지 등의 기능이 있다.
         */
        //1 page order by bno desc
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count : "+result.getTotalElements());
        log.info("total pages : "+result.getTotalPages());
        log.info("page numnber : "+result.getNumber());
        log.info("page size : "+result.getSize());

        List<Board> todoList = result.getContent();

        todoList.forEach(board -> log.info(board));
    }


    // 페이징 테스트 2 - Querydsl
    @Test
    public void search1Test(){
        //2 page order by bno desc
        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());

        boardRepository.search1(pageable);
    }


    // 검색 조건과 목록 처리 테스트 1 - Querydsl
    @Test
    public void searchAllTest(){

        String[] types = {"t", "c", "w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
    }


    // 검색 조건과 목록 처리 테스트 2 - PageImpl
    @Test
    public void searchAll2Test(){
        
        String[] types = {"t", "c", "w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        //total pages
        log.info(result.getTotalPages());

        //page size
        log.info(result.getSize());

        //pageNumber
        log.info(result.getNumber());

        //prev next
        log.info(result.hasPrevious()+" : "+result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }
}
