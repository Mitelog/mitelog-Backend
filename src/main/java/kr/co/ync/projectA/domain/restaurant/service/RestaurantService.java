package kr.co.ync.projectA.domain.restaurant.service;

import kr.co.ync.projectA.domain.category.entity.CategoryEntity;
import kr.co.ync.projectA.domain.category.repository.CategoryRepository;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.member.repository.MemberRepository;
import kr.co.ync.projectA.domain.restaurant.dto.request.RestaurantRequest;
import kr.co.ync.projectA.domain.restaurant.dto.response.RestaurantResponse;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.domain.restaurant.mapper.RestaurantMapper;
import kr.co.ync.projectA.domain.restaurant.repository.RestaurantRepository;
import kr.co.ync.projectA.domain.restaurantCategoryMapEntity.entity.RestaurantCategoryMapEntity;
import kr.co.ync.projectA.domain.restaurantCategoryMapEntity.repository.RestaurantCategoryMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ✅ RestaurantService
 * 식당 등록, 수정, 조회, 삭제를 담당.
 * 카테고리 매핑 로직 포함 (tbl_restaurant_category_map)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final RestaurantCategoryMapRepository restaurantCategoryMapRepository;

    /** ✅ 식당 등록 */
    @Transactional
    public RestaurantResponse register(RestaurantRequest request) {
        // ⚙️ TODO: JWT 인증 연결 후 실제 로그인 유저로 교체 예정
        MemberEntity owner = memberRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        // ✅ 식당 엔티티 생성 및 저장
        RestaurantEntity restaurant = RestaurantMapper.toEntity(request, owner);
        RestaurantEntity savedRestaurant = restaurantRepository.save(restaurant);

        // ✅ 선택된 카테고리 매핑 처리
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            List<CategoryEntity> categories = categoryRepository.findAllById(request.getCategoryIds());

            List<RestaurantCategoryMapEntity> mappings = categories.stream()
                    .map(category -> RestaurantCategoryMapEntity.builder()
                            .restaurant(savedRestaurant)
                            .category(category)
                            .build())
                    .toList();

            restaurantCategoryMapRepository.saveAll(mappings);
        }

        return RestaurantMapper.toResponse(savedRestaurant);
    }

    /** ✅ 식당 통합 검색 (키워드 + 지역 + 카테고리) */
    public Page<RestaurantResponse> searchRestaurants(String keyword, String region, String category, Pageable pageable) {
        if (keyword != null && keyword.trim().isEmpty()) keyword = null;
        if (region != null && region.trim().isEmpty()) region = null;
        if (category != null && category.trim().isEmpty()) category = null;

        Page<RestaurantEntity> restaurants = (keyword == null && region == null && category == null)
                ? restaurantRepository.findAll(pageable)
                : restaurantRepository.findByConditions(keyword, region, category, pageable);

        return restaurants.map(RestaurantMapper::toResponse);
    }

    /** ✅ 전체 조회 */
    public Page<RestaurantResponse> getAll(PageRequest pageRequest) {
        return restaurantRepository.findAll(pageRequest)
                .map(RestaurantMapper::toResponse);
    }

    /** ✅ 상세 조회 */
    public RestaurantResponse getById(Long id) {
        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없습니다."));
        return RestaurantMapper.toResponse(restaurant);
    }

    /** ✅ 수정 (카테고리 재매핑 포함) */
    @Transactional
    public RestaurantResponse update(Long id, RestaurantRequest request) {
        RestaurantEntity entity = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없습니다."));

        entity.update(
                request.getName(),
                request.getAddress(),
                request.getArea(),
                request.getPhone(),
                request.getImage()
        );

        RestaurantEntity saved = restaurantRepository.save(entity);

        // ✅ 기존 카테고리 매핑 전체 삭제
        restaurantCategoryMapRepository.deleteByRestaurantId(id);

        // ✅ 새 카테고리 매핑 등록
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            List<CategoryEntity> categories = categoryRepository.findAllById(request.getCategoryIds());
            List<RestaurantCategoryMapEntity> newMappings = categories.stream()
                    .map(cat -> RestaurantCategoryMapEntity.builder()
                            .restaurant(saved)
                            .category(cat)
                            .build())
                    .toList();
            restaurantCategoryMapRepository.saveAll(newMappings);
        }

        return RestaurantMapper.toResponse(saved);
    }

    /** ✅ 삭제 */
    @Transactional
    public void delete(Long id) {
        // ✅ 매핑 먼저 삭제 후 식당 삭제 (안전하게)
        restaurantCategoryMapRepository.deleteByRestaurantId(id);
        restaurantRepository.deleteById(id);
    }
}
