package kr.co.ync.projectA.domain.restaurant.service;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.member.repository.MemberRepository;
import kr.co.ync.projectA.domain.restaurant.dto.request.RestaurantRequest;
import kr.co.ync.projectA.domain.restaurant.dto.response.RestaurantResponse;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.domain.restaurant.mapper.RestaurantMapper;
import kr.co.ync.projectA.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;

    /** ✅ 식당 등록 */
    @Transactional
    public RestaurantResponse register(RestaurantRequest request) {
        // TODO: JWT 인증 연결 후 실제 로그인 유저 사용 예정
        MemberEntity owner = memberRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        RestaurantEntity entity = RestaurantMapper.toEntity(request, owner);
        RestaurantEntity saved = restaurantRepository.save(entity);
        return RestaurantMapper.toResponse(saved);
    }

    /** ✅ 통합 조회 (검색 + 필터 + 전체)
     *  프론트에서 아래 형태로 요청:
     *  /api/restaurants?keyword=ラーメン&region=東京都&category=和食
     */
    public Page<RestaurantResponse> searchRestaurants(
            String keyword,
            String region,
            String category,
            Pageable pageable
    ) {
        // 빈 문자열("")이면 null 처리 → JPQL에서 조건 무시
        if (keyword != null && keyword.trim().isEmpty()) keyword = null;
        if (region != null && region.trim().isEmpty()) region = null;
        if (category != null && category.trim().isEmpty()) category = null;

        Page<RestaurantEntity> restaurants;

        // 모든 필터가 비어 있으면 전체 조회
        if (keyword == null && region == null && category == null) {
            restaurants = restaurantRepository.findAll(pageable);
        } else {
            restaurants = restaurantRepository.findByConditions(keyword, region, category, pageable);
        }

        return restaurants.map(RestaurantMapper::toResponse);
    }

    /** ✅ 전체 조회 (기존 유지 — 필요 시 단독 호출 가능) */
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

    /** ✅ 수정 */
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
        return RestaurantMapper.toResponse(saved);
    }

    /** ✅ 삭제 */
    @Transactional
    public void delete(Long id) {
        restaurantRepository.deleteById(id);
    }
}
