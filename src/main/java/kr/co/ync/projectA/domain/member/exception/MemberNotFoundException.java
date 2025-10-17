package kr.co.ync.projectA.domain.member.exception;

import kr.co.ync.projectA.global.exception.CustomException;

public class MemberNotFoundException extends CustomException {
    public static final CustomException EXCEPTION = new MemberNotFoundException();
    private MemberNotFoundException(){
        super("해당 회원의 정보가 존재하지 않습니다.");
    }
}
