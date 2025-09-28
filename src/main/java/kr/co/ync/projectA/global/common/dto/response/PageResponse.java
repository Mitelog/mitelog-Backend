package kr.co.ync.projectA.global.common.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PageResponse<T> {
    private T data;
    private int page;
    private int size;
    private int total;
}
