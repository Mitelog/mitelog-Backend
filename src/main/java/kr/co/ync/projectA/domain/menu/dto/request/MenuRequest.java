package kr.co.ync.projectA.domain.menu.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuRequest {
    private String name;
    private int price;
    private String imageUrl;
}
