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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.dto.MemberJoinDTO;
import org.zerock.b01.service.MemberService;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    //의존성 주입
    private final MemberService memberService;

    //Remember-Me 토큰 정보 삭제를 위해 생성한 객체
    private final PersistentTokenRepository persistentTokenRepository;


    //로그인
    @GetMapping("/login")
    public void loginGET(String errorCode, String logout){
        log.info("login GET ..........");
        log.info("logout : " + logout);

        if(logout != null){
            log.info("user logout ..........");
        }
    }


    //로그아웃
    @GetMapping("/logout")
    public String logout() {

        return "redirect:/member/login?logout";
    }


    //회원가입 View
    @GetMapping("/join")
    public void joinView(){
        log.info("join View .....");
    }


    //회원가입 POST
    @PostMapping("/join")
    public String joinPOST(MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes){
        log.info("join post .....");
        log.info(memberJoinDTO);

        try {
            memberService.join(memberJoinDTO);
        } catch (MemberService.MidExistException e){
            redirectAttributes.addFlashAttribute("error", "mid");

            return "redirect:/member/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");

        return "redirect:/member/login";
    }
}
