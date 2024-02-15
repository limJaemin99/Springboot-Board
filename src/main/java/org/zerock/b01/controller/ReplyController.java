package org.zerock.b01.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.ReplyDTO;
import org.zerock.b01.service.ReplyService;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor    // 의존성 주입을 위함
public class ReplyController {

    private final ReplyService replyService;


    //댓글 작성
    @Operation(description = "POST 방식으로 댓글 등록")
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> register(@Valid @RequestBody ReplyDTO replyDTO,
                                                     BindingResult bindingResult) throws BindException {
        log.info(replyDTO);

        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }

        Long rno = replyService.register(replyDTO);
        Map<String,Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);

        return resultMap;
    }


    //특정 게시물의 댓글 목록
    @Operation(description = "GET 방식으로 특정 게시물의 댓글 목록")
    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO){
        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);

        return responseDTO;
    }


    //특정 댓글 조회
    @Operation(description = "GET 방식으로 특정 댓글 조회")
    @GetMapping("/{rno}")
    public ReplyDTO getReplyDTO(@PathVariable("rno") Long rno){
        ReplyDTO replyDTO = replyService.read(rno);

        return replyDTO;
    }


    //특정 댓글 삭제
    @Operation(description = "DELETE 방식으로 특정 댓글 삭제")
    @DeleteMapping("/{rno}")
    public Map<String,Long> remove(@PathVariable("rno") Long rno){
        /*
            데이터가 존재하지 않는데도 삭제 요청이 정상적으로 처리되고 SQL 쿼리가 실행되는 현상이 식별됨.
                이는 일반적으로 JPA나 Hibernate의 동작 방식 때문인데
                Hibernate는 JPA의 구현체 중 하나로, DB와의 상호작용을 관리하는데 사용된다.

                Hibernate는 다음과 같은 특징을 가지고 있다.
                    ① 지연 로딩 (Lazy Loading) : 관련된 엔티티나 컬렉션을 '실제로 사용할 때까지' DB에서 로딩을 지연시킴
                    ② 캐싱 (Cashing) : 쿼리나 엔티티를 캐시하여 성능을 향상시킨다.
                    ③ 영속성 컨텍스트 (Persistence Context) : 엔티티의 상태를 추적하고, 변경 내용을 DB에 동기화한다.
                    ④ 영속성 제어 (Persistence Control) : 엔티티 상태 변화를 추적하여 적절한 SQL 쿼리를 생성 및 실행한다.

                이런 특징들로 인해 [실제 DV에 없는 데이터를 삭제하려는 요청이 Hibernate에 의해 무시될 수 있다.]
                따라서 아래와 같이 데이터의 존재 여부를 미리 확인하고, 존재하지 않는다면 EmptyResultDataAccessException를 발생시킨다.
         */
        try{
            replyService.read(rno);
        } catch (NoSuchElementException e){
            log.error("DELETE Error ..... EmptyResultDataAccessException");
            throw new EmptyResultDataAccessException(1);
        }

        replyService.remove(rno);

        Map<String,Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);

        return resultMap;
    }


    //특정 댓글 수정
    @Operation(description = "PUT 방식으로 특정 댓글 수정")
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> modify(@PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO){
        replyDTO.setRno(rno);   //번호를 일치시킴
        replyService.modify(replyDTO);

        Map<String,Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);

        return resultMap;
    }
}
