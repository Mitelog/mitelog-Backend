package kr.co.ync.projectA.domain.restaurant.service;

import kr.co.ync.projectA.domain.restaurant.dto.Restaurant;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.domain.restaurant.mapper.RestaurantMapper;
import kr.co.ync.projectA.domain.restaurant.repository.RestaurantRepository;
import kr.co.ync.projectA.domain.member.repository.MemberRepository;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository; // ownerId를 DTO에서 받기 때문

    /**
     * 식당 등록
     */
    public Restaurant register(Restaurant dto) {
        // ownerId로 MemberEntity 조회
        MemberEntity owner = memberRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));

        // DTO → Entity 변환
        RestaurantEntity entity = RestaurantMapper.toEntity(dto);
        entity.setOwner(owner);

        // 저장
        RestaurantEntity saved = restaurantRepository.save(entity);

        // Entity → DTO 변환 후 반환
        return RestaurantMapper.toDTO(saved);
    }

    /**
     * 식당 전체 조회
     */
    public Page<Restaurant> getAll(PageRequest pageRequest) {
        Page<RestaurantEntity> page = restaurantRepository.findAll(pageRequest);
        return page.map(RestaurantMapper::toDTO);
    }

    /**
     * 식당 상세 조회
     */
    public Restaurant getById(Long id) {
        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없습니다."));
        return RestaurantMapper.toDTO(restaurant);
    }

    /**
     * 식당 삭제
     */
    public void delete(Long id) {
        restaurantRepository.deleteById(id);
    }

    /**
     * 식당 수정
     */
    public Restaurant update(Long id, Restaurant updated) {
        RestaurantEntity entity = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없습니다."));

        entity.update(
                updated.getName(),
                updated.getAddress(),
                updated.getArea(),
                updated.getPhone(),
                updated.getImage()
        );

        RestaurantEntity saved = restaurantRepository.save(entity);
        return RestaurantMapper.toDTO(saved);
    }

    /**
     * 지역별 조회
     */
    public List<Restaurant> getByArea(String area) {
        return restaurantRepository.findByArea(area).stream()
                .map(RestaurantMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리별 조회 (페이징)
     */
    public Page<Restaurant> getByCategory(String category, PageRequest pageRequest) {
        Page<RestaurantEntity> page = restaurantRepository.findByCategoryName(category, pageRequest);
        return page.map(RestaurantMapper::toDTO);
    }

    /**
     * 이름 검색
     */
    public List<Restaurant> searchByName(String keyword) {
        return restaurantRepository.findByNameContaining(keyword).stream()
                .map(RestaurantMapper::toDTO)
                .collect(Collectors.toList());
    }
}
