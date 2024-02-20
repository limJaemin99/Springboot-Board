package org.zerock.b01.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
//@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    //PasswordEncoder를 잠깐 테스트하는 용도로 사용하므로 BCrypPasswordEncoder를 생성
    private PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(){
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /*
        loadUserByUsername()의 리턴 타입은 org.springframework.security.core.userdetails.UserDetails라는 인터페이스이다.
            UserDetails는 [사용자 인증(Authentication)과 관련된 정보들을 저장]하는 역할이다.

        Spring Security는 내부적으로 UserDetails 타입의 객체를 이용해서 PW를 검사하고, 사용자 권한을 확인하는 방식으로 동작한다.
            UserDetails 인터페이스의 추상 메소드들은 다음과 같다.
                - getAuthorities() : 사용자에게 부여된 권한(인가)을 반환   (Return Collection<? extends GrantedAuthority>)
                - getPassword() : 사용자를 인증하는 데 사용되는 password를 반환 (Return String)
                - getUsername() : 사용자를 인증하는 데 사용되는 username을 반환 (Return String)
                - isAccountNonExpired() : 사용자 계정의 만료 여부를 반환 (Return boolean)
                - isAccountNonLocked() : 사용자의 잠금 여부를 반환 (Return boolean)
                - isCredentialsNonExpired() : 사용자의 자격 증명(비밀번호)이 만료되었는지 여부를 반환   (Return boolean)
                - isEnabled() : 사용자의 활성화 여부를 반환 (Return boolean)
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername : " + username);

        //User 클래스를 이용한 로그인 처리 (UserDetails 인터페이스를 구현)
        UserDetails userDetails = User.builder()
                    .username("user1")
                    //.password("1111")
                    .password(passwordEncoder.encode("1111"))   //패스워드 인코딩 필요
                    .authorities("ROLE_USER")
                    .build();

        return userDetails;
    }
}
