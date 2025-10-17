package kr.co.ync.projectA.global.jwt.exception;


import kr.co.ync.projectA.global.exception.CustomException;

public class TokenTypeException extends CustomException {

    public static final CustomException EXCEPTION = new TokenTypeException();

    public TokenTypeException() {
        super("잘못된 JWT 타입");
    }
}
