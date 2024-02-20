package org.zerock.b01.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Log4j2
public class Custom403Handler implements AccessDeniedHandler {

    /*
        ● 403(Fobidden)에러는 서버에서 사용자의 요청을 거부했다는 의미이다.
            예시) user1로 로그인 후 user2의 게시글을 수정할 경우 발생
                    즉, 현재 사용자가 로그인은 되었지만 해당 작업을 수행할 수 없는 경우에 해당

        Spring Security에서 @PreAuthorize("isAuthenticated()")인 경우 사용자 로그인이 안 되었다면
            302 메시지와 함께 로그인 경로로 이동하지만, 403 에러는 Whitelabel Error Page로 이동한다.

        따라서 에러 페이지를 보여주는 대신에 AccessDeniedHandler 인터페이스를 구현해서 상황에 맞게 처리해보겠다.
            - <form> 태그의 요청이 403인 경우, 로그인 페이지로 이동할 때 'ACCESS_DENIED'값을 파라미터로 같이 전달
            - Ajax인 경우에는 JSON 데이터를 만들어서 전송
     */

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException
    {
        log.info("━━━━━━━━━━━━━━━━━━━━ Access Denied ━━━━━━━━━━━━━━━━━━━━");

        response.setStatus(HttpStatus.FORBIDDEN.value());

        //JSON 요청이었는지 확인
        String contentType = request.getHeader("Content-Type");
        boolean jsonRequest = contentType.startsWith("application/json");

        log.info("isJSON : "+jsonRequest);

        //일반 request
        if(!jsonRequest){
            response.sendRedirect("/member/login?error=ACCESS_DENIED");
        }
    }
}
