package kr.co.ync.projectA.domain.restaurant.service;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.member.repository.MemberRepository;
import kr.co.ync.projectA.domain.restaurant.dto.request.RestaurantRequest;
import kr.co.ync.projectA.domain.restaurant.dto.response.RestaurantResponse;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.domain.restaurant.mapper.RestaurantMapper;
import kr.co.ync.projectA.domain.restaurant.repository.RestaurantRepository;
import kr.co.ync.projectA.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ✅ Restaurant 관련 비즈니스 로직을 처리하는 서비스 클래스
 * (인터페이스와 구현체를 통합한 단일 클래스 버전)
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    /**
     * ✅ 식당 등록
     */
    @Transactional
    public RestaurantResponse register(RestaurantRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        MemberEntity owner = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        RestaurantEntity entity = RestaurantMapper.toEntity(request, owner);
        RestaurantEntity saved = restaurantRepository.save(entity);

        return RestaurantMapper.toResponse(saved);
    }

    /**
     * ✅ 전체 조회
     */
    public Page<RestaurantResponse> getAll(PageRequest pageRequest) {
        return restaurantRepository.findAll(pageRequest)
                .map(RestaurantMapper::toResponse);
    }

    /**
     * ✅ 상세 조회
     */
    public RestaurantResponse getById(Long id) {
        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없습니다."));
        int reviewCount = reviewRepository.countByRestaurantId(id);

        return RestaurantMapper.toResponse(restaurant, reviewCount);
    }

    /**
     * ✅ 수정
     */
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

    /**
     * ✅ 삭제
     */
    @Transactional
    public void delete(Long id) {
        restaurantRepository.deleteById(id);
    }

    /**
     * ✅ 지역별 조회
     */
    public List<RestaurantResponse> getByArea(String area) {
        return restaurantRepository.findByArea(area)
                .stream()
                .map(RestaurantMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * ✅ 카테고리별 조회
     */
    public Page<RestaurantResponse> getByCategory(String category, PageRequest pageRequest) {
        return restaurantRepository.findByCategoryName(category, pageRequest)
                .map(RestaurantMapper::toResponse);
    }

    /**
     * ✅ 이름 검색
     */
    public List<RestaurantResponse> searchByName(String keyword) {
        return restaurantRepository.findByNameContaining(keyword)
                .stream()
                .map(RestaurantMapper::toResponse)
                .collect(Collectors.toList());
    }

    public Page<RestaurantResponse> findByOwnerId(Long ownerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<RestaurantEntity> restaurantPage = restaurantRepository.findByOwnerId(ownerId, pageable);
        return restaurantPage.map(RestaurantResponse::fromEntity);
    }

    // ================== 메인 페이지용 ==================

    /**
     * 🥇 인기 식당 (평점 desc, 평점 같으면 리뷰 수 많은 순)
     */
    public List<RestaurantResponse> getPopularRestaurants(int limit) {
        // 전체 식당 조회
        List<RestaurantEntity> all = restaurantRepository.findAll();

        // 평점 → 리뷰 수 → id 순 정렬
        all.sort((r1, r2) -> {
            double avg1 = r1.getAverageRating() != null ? r1.getAverageRating() : 0.0;
            double avg2 = r2.getAverageRating() != null ? r2.getAverageRating() : 0.0;

            // 1순위: 평점 내림차순
            int cmp = Double.compare(avg2, avg1);
            if (cmp != 0) return cmp;

            // 2순위: 리뷰 수 내림차순
            int c1 = reviewRepository.countByRestaurantId(r1.getId());
            int c2 = reviewRepository.countByRestaurantId(r2.getId());
            cmp = Integer.compare(c2, c1);
            if (cmp != 0) return cmp;

            // 3순위: id 내림차순 (더 최신이 앞으로 오게)
            return Long.compare(r2.getId(), r1.getId());
        });

        return all.stream()
                .limit(limit)
                .map(RestaurantResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 🆕 신규 식당 (생성일자 최신 순)
     */
    public List<RestaurantResponse> getNewRestaurants(int limit) {
        PageRequest pageRequest = PageRequest.of(
                0,
                limit,
                Sort.by(Sort.Direction.DESC, "createDateTime")
        );

        return restaurantRepository.findAll(pageRequest)
                .stream()
                .map(RestaurantResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
