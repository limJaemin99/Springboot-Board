package org.zerock.b01.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class CustomSecurityConfig {
    /*
        Spring Security의 경우 단순히 application.properties를 이용하는 설정보다
            코드를 이용해서 설정을 조정하는 경우가 더 많기 때문에 별도의 클래스를 이용해서 설정을 조정한다.

        * 이 클래스는 @Configuration을 적용한다.

        설정이 완료된 후, 프로젝트를 실행하면 다음과 같이 알 수 없는 password가 생성 및 출력된다.
            ▶ Using generated security password: 47a47ad8-4f9e-49e5-bfa8-b0ad10af27b0
                ※ 값은 매번 다르게 생성되며, 기본적으로 생성되는 사용자의 아이디는 user 이다.

        실행된 프로젝트를 보면 기존에 아무 문제 없이 접근할 수 있는 '/board/list'에 로그인 처리가 필요한 것을 알 수 있다.
            Spring Security는 별도의 설정이 없을 땐 모든 자원에 필요한 권한이나 로그인 여부 등을 확인한다.
            처리 과정을 살펴보면 '/board/list'를 호출했지만, '/login'경로로 리다이렉트 되는 것을 볼 수 있다.
                (이때, 'user'라는 아이디와 생성된 password로 로그인할 수 있다.)
            로그인하지 않아도 볼 수 있도록 설정하고 싶다면 개발자가 직접 설정하는 코드가 반드시 있어야한다.
                * CustomSecurityConfig에 [SecurityFilterChain]이라는 객체를 반환하는 메소드를 작성한다. ▶ filterChain()
            filterChain() 메소드가 동작하면 이전과 달리 '/board/list'에 바로 접근할 수 있다.

            * 즉, CustomSecurityConfig 의 filterChain() 메소드 설정으로, 모든 사용자가 모든 경로에 접근할 수 있게 된다.
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("━━━━━━━━━━━━━━━━━━━━ Configure ━━━━━━━━━━━━━━━━━━━━");

        return http.build();
    }

    /*
        프로젝트에서 완전히 정적으로 동작하는 파일들에는 굳이 시큐리티를 적용할 필요가 없다.
            이런 경우 CustomSecurityConfig에 webSecurityCustomizer() 메소드 설정을 추가한다.

        configure(WebSecurity web)를 아래와 같이 설정하면, 정적 자원들은 Spring Security 적용에서 제외시킬 수 있다.
            예시) '/css/styles.css'를 호출할 때는 필터가 동작하지 않는다.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        log.info("━━━━━━━━━━━━━━━━━━━━ Web Configure ━━━━━━━━━━━━━━━━━━━━");

        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
