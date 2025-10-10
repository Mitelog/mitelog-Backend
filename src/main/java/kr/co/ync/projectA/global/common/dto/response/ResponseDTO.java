package kr.co.ync.projectA.global.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDTO<T> {
    private int status;   // 상태 코드 (예: 200, 400 등)
    private String msg;   // 메시지 (예: "성공", "실패", "인증 오류")
    private T data;       // 실제 데이터 (DTO, List, Object 등)
}
