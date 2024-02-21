package org.zerock.b01.service;

import org.zerock.b01.dto.MemberJoinDTO;

public interface MemberService {

    /*
        회원 가입시 이미 해당 아이디가 존재할 경우 MemberRepository의 save()는 insert가 아니라 update로 실행된다.
            만약 같은 아이디가 존재하면 MidExistException라는 예외를 발생시키도록 한다.
     */
    static class MidExistException extends Exception {

    }

    void join(MemberJoinDTO memberJoinDTO) throws MidExistException;

}
