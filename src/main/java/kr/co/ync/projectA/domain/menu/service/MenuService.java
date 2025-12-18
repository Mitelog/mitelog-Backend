package kr.co.ync.projectA.domain.menu.service;

import kr.co.ync.projectA.domain.menu.dto.request.MenuRequest;
import kr.co.ync.projectA.domain.menu.dto.response.MenuResponse;
import kr.co.ync.projectA.domain.menu.entity.MenuEntity;
import kr.co.ync.projectA.domain.menu.mapper.MenuMapper;
import kr.co.ync.projectA.domain.menu.repository.MenuRepository;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.domain.restaurant.repository.RestaurantRepository;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    /**
     * 특정 레스토랑의 메뉴 목록 조회
     */
    public List<MenuResponse> getMenusByRestaurant(Long restaurantId) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        List<MenuEntity> menus = menuRepository.findByRestaurant(restaurant);

        return menus.stream()
                .map(MenuMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * 메뉴 등록 (가게 주인만)
     */
    @Transactional
    public MenuResponse addMenu(Long restaurantId, MenuRequest dto, MemberEntity loginUser) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        if (!restaurant.getOwner().getId().equals(loginUser.getId())) {
            throw new RuntimeException("해당 가게의 주인만 메뉴를 추가할 수 있습니다.");
        }

        MenuEntity menu = MenuMapper.toEntity(dto, restaurant);
        menuRepository.save(menu);
        return MenuMapper.toResponse(menu);
    }

    /**
     * 메뉴 수정 (가게 주인만)
     */
    @Transactional
    public MenuResponse updateMenu(Long menuId, MenuRequest dto, MemberEntity loginUser) {
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new RuntimeException("Menu not found"));
        RestaurantEntity restaurant = menu.getRestaurant();

        if (!restaurant.getOwner().getId().equals(loginUser.getId())) {
            throw new RuntimeException("해당 가게의 주인만 메뉴를 수정할 수 있습니다.");
        }

        MenuMapper.updateEntity(menu, dto);
        return MenuMapper.toResponse(menu);
    }

    @Transactional
    public void deleteMenu(Long menuId, MemberEntity loginUser) {
        MenuEntity menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found"));

        // ✅ "가게 주인만" 조건: 메뉴 -> 레스토랑 -> owner 체크
        if (!menu.getRestaurant().getOwner().getId().equals(loginUser.getId())) {
            throw new AccessDeniedException("Not owner");
        }

        menuRepository.delete(menu);
    }

}
