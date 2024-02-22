package org.zerock.b01.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.ui.Model;
import org.zerock.b01.security.dto.MemberSecurityDTO;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException
    {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("CustomSocialLoginSuccessHandler onAuthenticationSuccess ..........");
        log.info(authentication.getPrincipal());

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();
        String encodedPw = memberSecurityDTO.getMpw();

        //소셜 로그인이고, 회원의 패스워드가 1111 ▶ 회원 정보 수정으로 이동
        if(memberSecurityDTO.isSocial()
                && memberSecurityDTO.getMpw().equals("1111")
                || passwordEncoder.matches("1111", memberSecurityDTO.getMpw()))
        {
            log.info("Should Change Password");

            log.info("Redirect to Member Modify");
            request.setAttribute("mid", memberSecurityDTO.getMid());
            request.setAttribute("email", memberSecurityDTO.getEmail());
            request.setAttribute("message", "<<소셜 로그인입니다.>>\n비밀번호를 설정해주세요");
            request.getRequestDispatcher("/member/modify").forward(request, response);
            //response.sendRedirect("/member/modify");

            return;
        } else {    //아닌 경우 /board/list로 이동
            response.sendRedirect("/board/list");
        }
    }

}
