package kr.co.ync.projectA.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@ToString
public class BoardResponse {
    private Long boardId;
    private String title;
    private String email;
    private String name;
    private long replyCount;
    private LocalDateTime createdDateTime;
}
