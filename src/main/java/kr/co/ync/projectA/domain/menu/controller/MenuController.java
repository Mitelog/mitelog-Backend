package kr.co.ync.projectA.domain.menu.controller;

import kr.co.ync.projectA.domain.menu.dto.request.MenuRequest;
import kr.co.ync.projectA.domain.menu.dto.response.MenuResponse;
import kr.co.ync.projectA.domain.menu.service.MenuService;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.global.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /**
     * ✅ 메뉴 조회 (누구나 가능)
     */
    @GetMapping("/restaurant/{restaurantId}")
    public List<MenuResponse> getMenus(@PathVariable Long restaurantId) {
        return menuService.getMenusByRestaurant(restaurantId);
    }

    /**
     * ✅ 메뉴 등록 (가게 주인만)
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/restaurant/{restaurantId}")
    public MenuResponse addMenu(
            @PathVariable Long restaurantId,
            @RequestBody MenuRequest dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        MemberEntity loginUser = userDetails.getMember();
        return menuService.addMenu(restaurantId, dto, loginUser);
    }

    /**
     * ✅ 메뉴 수정 (가게 주인만)
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{menuId}")
    public MenuResponse updateMenu(
            @PathVariable Long menuId,
            @RequestBody MenuRequest dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        MemberEntity loginUser = userDetails.getMember();
        return menuService.updateMenu(menuId, dto, loginUser);
    }
}
