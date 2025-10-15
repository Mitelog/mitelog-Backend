package kr.co.ync.projectA.domain.menu.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuResponse {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
}
