package org.zerock.b01.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.b01.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String > {

    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.mid = :mid and m.social = false")
    Optional<Member> getWithRoles(String mid);


    //email로 회원 정보 찾기
    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> findByEmail(String email);


    //소셜 로그인 사용자의 비밀번호 변경
        // @Query는 주로 select 할 때 이용하지만, @Modifying과 같이 사용하면 DML(Insert,Update,Delete) 처리도 가능하다.
    @Modifying
    @Transactional
    @Query("update Member m set m.mpw =:mpw where m.mid =:mid")
    void updatePassword(@Param("mpw") String password, @Param("mid") String mid);

}
