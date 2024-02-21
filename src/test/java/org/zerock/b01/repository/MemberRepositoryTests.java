package org.zerock.b01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.zerock.b01.domain.Member;
import org.zerock.b01.domain.MemberRole;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    //일반 회원 추가 테스트
    @Test
    public void insertMembersTest(){

        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = Member.builder()
                    .mid("member"+i)
                    .mpw(passwordEncoder.encode("1111"))
                    .email("email"+i+"@test.com")
                    .build();

            member.addRole(MemberRole.USER);

            if(i >= 90){
                member.addRole(MemberRole.ADMIN);
            }

            memberRepository.save(member);
        });

    }


    //회원 조회 테스트
    @Test
    public void readTest(){
        //mid가 'member100'인 사용자는 USER,ADMIN 권한을 모두 가지고있는데 이를 확인하는 테스트 코드
        Optional<Member> result = memberRepository.getWithRoles("member100");

        Member member = result.orElseThrow();

        log.info(member);
        log.info(member.getRoleSet());

        member.getRoleSet().forEach(memberRole -> log.info(memberRole.name()));
    }


    //소셜 로그인한 사용자의 비밀번호 변경
    @Test
    @Commit
    public void updateTest(){
        //소셜 로그인으로 추가된 사용자의 mid
        String mid = "limjaemin99@gmail.com";

        //소셜 로그인으로 추가된 사용자의 mpw
        String mpw = passwordEncoder.encode("54321");

        memberRepository.updatePassword(mpw, mid);
    }
    
}
