package kr.co.ync.projectA.domain.restaurant.service;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.member.repository.MemberRepository;
import kr.co.ync.projectA.domain.restaurant.dto.request.RestaurantRequest;
import kr.co.ync.projectA.domain.restaurant.dto.request.RestaurantSearchRequest;
import kr.co.ync.projectA.domain.restaurant.dto.response.RestaurantResponse;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.domain.restaurant.mapper.RestaurantMapper;
import kr.co.ync.projectA.domain.restaurant.repository.RestaurantQueryRepository;
import kr.co.ync.projectA.domain.restaurant.repository.RestaurantRepository;
import kr.co.ync.projectA.domain.review.repository.ReviewRepository;
import kr.co.ync.projectA.domain.restaurantDetail.dto.response.RestaurantDetailResponse;
import kr.co.ync.projectA.domain.restaurantDetail.service.RestaurantDetailService;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;

    // ✅ 추가: Detail 서비스 주입
    private final RestaurantDetailService restaurantDetailService;

    private final RestaurantQueryRepository restaurantQueryRepository;

    /**
     * ✅ 식당 등록
     */
    @Transactional
    public RestaurantResponse register(RestaurantRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        MemberEntity owner = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        // 1) Restaurant 저장
        RestaurantEntity entity = RestaurantMapper.toEntity(request, owner);
        RestaurantEntity saved = restaurantRepository.save(entity);

        // 2) ✅ RestaurantDetail upsert (없으면 생성, 있으면 수정)
        // - request.getDetail()이 null이면 RestaurantDetailService에서 그냥 return 처리됨
        restaurantDetailService.upsertDetail(saved, request.getDetail());

        // 3) ✅ 응답에 detail 포함 (표시까지 바로 연결하려면 여기서도 세팅)
        RestaurantDetailResponse detail = restaurantDetailService.getDetailOrNull(saved.getId());

        // 기존 mapper 결과 + detail 합치기
        RestaurantResponse base = RestaurantMapper.toResponse(saved);
        return RestaurantResponse.builder()
                .id(base.getId())
                .ownerId(base.getOwnerId())
                .ownerEmail(base.getOwnerEmail())
                .ownerName(base.getOwnerName())
                .name(base.getName())
                .address(base.getAddress())
                .area(base.getArea())
                .phone(base.getPhone())
                .image(base.getImage())
                .averageRating(base.getAverageRating())
                .categoryNames(base.getCategoryNames())
                .reviewCount(base.getReviewCount())
                .detail(detail)
                .build();
    }

    /**
     * ✅ 전체 조회
     */
    public Page<RestaurantResponse> getAll(RestaurantSearchRequest cond, Pageable pageable) {
        // ✅ 조건이 하나도 없으면 기존 전체조회 로직 그대로 사용(가벼움)
        boolean noFilter =
                (cond == null)
                        || (isBlank(cond.getKeyword())
                        && isBlank(cond.getArea())
                        && isBlank(cond.getCategory())
                        && cond.getCreditCard() == null
                        && cond.getParkingArea() == null
                        && cond.getPrivateRoom() == null
                        && cond.getSmoking() == null
                        && cond.getUnlimitDrink() == null
                        && cond.getUnlimitFood() == null);

        if (noFilter) {
            return restaurantRepository.findAll(pageable)
                    .map(RestaurantMapper::toResponse);
        }
        
        return restaurantQueryRepository.search(cond, pageable);
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    /**
     * ✅ 상세 조회
     */
    public RestaurantResponse getById(Long id) {
        RestaurantEntity restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없습니다."));
        int reviewCount = reviewRepository.countByRestaurantId(id);

        // ✅ detail 같이 내려주기
        RestaurantDetailResponse detail = restaurantDetailService.getDetailOrNull(id);

        RestaurantResponse base = RestaurantMapper.toResponse(restaurant, reviewCount);
        return RestaurantResponse.builder()
                .id(base.getId())
                .ownerId(base.getOwnerId())
                .ownerEmail(base.getOwnerEmail())
                .ownerName(base.getOwnerName())
                .name(base.getName())
                .address(base.getAddress())
                .area(base.getArea())
                .phone(base.getPhone())
                .image(base.getImage())
                .averageRating(base.getAverageRating())
                .categoryNames(base.getCategoryNames())
                .reviewCount(base.getReviewCount())
                .detail(detail)
                .build();
    }

    /**
     * ✅ 수정
     */
    @Transactional
    public RestaurantResponse update(Long id, RestaurantRequest request) {
        RestaurantEntity entity = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없습니다."));

        // 1) Restaurant 업데이트
        entity.update(
                request.getName(),
                request.getAddress(),
                request.getArea(),
                request.getPhone(),
                request.getImage()
        );

        RestaurantEntity saved = restaurantRepository.save(entity);

        // 2) ✅ RestaurantDetail upsert
        restaurantDetailService.upsertDetail(saved, request.getDetail());

        // 3) ✅ 응답에 detail 포함
        RestaurantDetailResponse detail = restaurantDetailService.getDetailOrNull(saved.getId());

        RestaurantResponse base = RestaurantMapper.toResponse(saved);
        return RestaurantResponse.builder()
                .id(base.getId())
                .ownerId(base.getOwnerId())
                .ownerEmail(base.getOwnerEmail())
                .ownerName(base.getOwnerName())
                .name(base.getName())
                .address(base.getAddress())
                .area(base.getArea())
                .phone(base.getPhone())
                .image(base.getImage())
                .averageRating(base.getAverageRating())
                .categoryNames(base.getCategoryNames())
                .reviewCount(base.getReviewCount())
                .detail(detail)
                .build();
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
        return restaurantRepository.findByArea(area).stream().map(RestaurantMapper::toResponse).collect(Collectors.toList());
    }

    /**
     * ✅ 카테고리별 조회
     */
    public Page<RestaurantResponse> getByCategory(String category, PageRequest pageRequest) {
        return restaurantRepository.findByCategoryName(category, pageRequest).map(RestaurantMapper::toResponse);
    }

    /**
     * ✅ 이름 검색
     */
    public List<RestaurantResponse> searchByName(String keyword) {
        return restaurantRepository.findByNameContaining(keyword).stream().map(RestaurantMapper::toResponse).collect(Collectors.toList());
    }

    public Page<RestaurantResponse> findByOwnerId(Long ownerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<RestaurantEntity> restaurantPage = restaurantRepository.findByOwnerId(ownerId, pageable);
        return restaurantPage.map(RestaurantResponse::fromEntity);
    }

    /**
     * 🥇 인기 식당 (평점 desc, 평점 같으면 리뷰 수 많은 순)
     */
    public List<RestaurantResponse> getPopularRestaurants(int limit) {
        List<RestaurantEntity> all = restaurantRepository.findAll();
        all.sort((r1, r2) -> {
            double avg1 = r1.getAverageRating() != null ? r1.getAverageRating() : 0.0;
            double avg2 = r2.getAverageRating() != null ? r2.getAverageRating() : 0.0;
            int cmp = Double.compare(avg2, avg1);
            if (cmp != 0) return cmp;
            int c1 = reviewRepository.countByRestaurantId(r1.getId());
            int c2 = reviewRepository.countByRestaurantId(r2.getId());
            cmp = Integer.compare(c2, c1);
            if (cmp != 0) return cmp;
            return Long.compare(r2.getId(), r1.getId());
        });
        return all.stream().limit(limit).map(RestaurantResponse::fromEntity).collect(Collectors.toList());
    }

    /**
     * 🆕 신규 식당 (생성일자 최신 순)
     */
    public List<RestaurantResponse> getNewRestaurants(int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createDateTime"));
        return restaurantRepository.findAll(pageRequest).stream().map(RestaurantResponse::fromEntity).collect(Collectors.toList());
    }

    // 나머지 메서드들은 일단 그대로 두고(리스트 필터는 3번에서),
    // 필요해지면 list 응답에도 detail 포함 여부를 결정하면 됨.
}
