package org.zerock.b01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.zerock.b01.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.b01.repository.search.BoardSearch;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {
    /*
        Spring Data JPA를 이용할 때는 JpaRepository라는 인터페이스를 이용하여 DB관련 작업을 처리할 수 있다.
            (이는 MyBatis를 이용할 때 mapper 인터페이스만을 선언하는 것과 유사하다.)
        [개발 단계에서 JpaRepository 인터페이스를 상속하는 인터페이스를 선언하는 것만으로 CRUD와 페이징 처리가 모두 완료된다.]
     */


    /*  쿼리 메소드와 @Query
        - 쿼리 메소드는 보통 SQL에서 사용하는 키워드와 컬럼들을 같이 결합해서 구성하면, 그 자체가 JPA에서 사용하는 쿼리가 되는 기능이다.
            일반적으로 메소드 이름은 'findBy...' 또는 'get...' 으로 시작하고, 컬럼명과 키워드를 결합하는 방식으로 구성한다.
        - 쿼리 메소드는 실제로 사용하려면 상당히 길고 복잡한 메소드를 작성하게 되는 경우가 많다.
            예시) Page<Board> findByTitleContainingOrderByBnoDesc(String keyword, Pageable pageable);
            따라서 쿼리메소드는 주로 단순한 쿼리를 작성할 때 사용하고, 실제 개발에서는 많이 사용되지 않는다.
        - 쿼리 메소드와 유사하게 별도의 처리 없이 @Query로 JPQL을 이용할 수 있다.
            @Query 어노테이션의 value로 작성하는 문자열을 JPQL이라고 하는데, SQL과 유사하게 JPA에서 사용하는 쿼리 언어이다.
            JPA는 DB에 독립적으로 개발이 가능하므로 특정한 DB에서만 동작하는 SQL 대신 JPA에 맞게 사용하는 JPQL을 이용하는 것이다.
        - JPQL은 테이블 대신에 엔티티 타입을 이용하고, 컬럼 대신에 엔티티의 속성을 이용해서 작성된다.

        @Query를 이용하면 크게 쿼리 메소드가 할 수 없는 몇 가지 기능을 할 수 있다.
            * 조인과 같이 복잡한 쿼리를 실행할 수 있는 기능
            * 원하는 속성들만 추출해서 Object[]로 처리하거나 DTO로 처리하는 기능
            * nativeQuery 속성값을 true로 지정해서 특정 데이터베이스에서 동작하는 SQL을 사용하는 기능
                 예시) getTime()
     */
    @Query("select b from Board b where b.title like concat('%',:keyword,'%')")
    Page<Board> findKeyword(String keyword, Pageable pageable);

    @Query(value = "select now()", nativeQuery = true)
    String getTime();
}
