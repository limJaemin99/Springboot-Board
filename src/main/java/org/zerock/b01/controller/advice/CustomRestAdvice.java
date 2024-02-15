package org.zerock.b01.controller.advice;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Log4j2
public class CustomRestAdvice {
    /*
        REST 방식의 컨트롤러는 대부분 Ajax와 같이 눈에 보이지 않는 방식으로 서버를 호출하고 결과를 전송하기 때문에
            에러 발생시 어디에서 어떤 에러가 발생했는지 알아보기가 힘들다.
            따라서 @Valid 과정에서 문제가 발생하면 처리할 수 있도록 @RestControllerAdvice를 설계해 두는것이 좋다.
     */


    /*
        게시물 등록시 Null값이나 Empty String이 넘어올 경우 BindException이 발생하는데
            아래와 같이 Exception handler로 처리할 수 있다.
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String,String>> handleBindException(BindException e){
        log.error(e);

        Map<String,String> errorMap = new HashMap<>();

        if(e.hasErrors()){
            BindingResult bindingResult = e.getBindingResult();

            bindingResult.getFieldErrors().forEach(fieldError -> {
                errorMap.put(fieldError.getField(), fieldError.getCode());
            });
        }

        return ResponseEntity.badRequest().body(errorMap);
    }

    /*
        사용할 수 없는 bno 값으로 댓글 작성시 SQLException이 발생하지만
            SQL만으로 보면, 없는 PK값을 사용했으므로 DataIntegrityViolationException 예외가 발생한다.
            하지만 서버의 상태 코드는 500으로 '서버 내부 오류'로 처리된다.

            외부에서 Ajax로 댓글 등록 기능을 호출했을 때 500에러가 발생한다면
            호출한 측에서는 서버의 문제로 생각하고, 전송하는 데이터에 문제가 있다고 생각하지는 않을 것이다.

            클라이언트에 서버의 문제가 아닌 데이터의 문제가 있다고 전송하기 위해서는
            CustomRestAdvice.java 파일에 아래와 같이 Exception handler 처리를 해줘야 한다.
    */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String,String>> handleFKException(Exception e){
        log.error(e);

        Map<String,String> errorMap = new HashMap<>();

        errorMap.put("time", ""+System.currentTimeMillis());
        errorMap.put("msg", "constraint fails");

        return ResponseEntity.badRequest().body(errorMap);
    }

    /*
        ① 존재하지 않는 rno 조회시 500에러가 발생한다.
            서비스 계층에서 조회시 Optional<T>을 이용했고 orElseThrow()를 이용했기 때문에
            컨트롤러에게 예외가 전달되고 NoSuchElementException이 발생한다.
            NoSuchElementException을 처리하기 위해 아래와 같이 Exception handler 처리를 해준다.

        ② 존재하지 않는 rno 댓글을 삭제시 500에러가 발생하는데
            아래 Exception handler에 EmptyResultDataAccessException를 추가하여 처리하도록 한다.

        ③ 존재하지 않는 rno 댓글 수정시 발생하는 오류도 아래 handler로 처리가 가능하다.
     */
    @ExceptionHandler({
            NoSuchElementException.class,
            EmptyResultDataAccessException.class
    })
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ResponseEntity<Map<String,String>> handleNoSuchElement(Exception e){
        log.error(e);

        Map<String,String> errorMap = new HashMap<>();

        errorMap.put("time", ""+System.currentTimeMillis());
        errorMap.put("msg", "No Such Element Exception");

        return ResponseEntity.badRequest().body(errorMap);
    }
}
