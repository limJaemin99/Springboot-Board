package org.zerock.b01.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    //Remember-Me 토큰 정보 삭제를 위해 생성한 객체
    private final PersistentTokenRepository persistentTokenRepository;


    @GetMapping("/login")
    public void loginGET(String errorCode, String logout){
        log.info("login GET ..........");
        log.info("logout : " + logout);

        if(logout != null){
            log.info("user logout ..........");
        }
    }


    @GetMapping("/logout")
    public String logout() {

        return "redirect:/member/login?logout";
    }
}
