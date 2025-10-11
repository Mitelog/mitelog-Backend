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

    /** ✅ 식당 등록 */
    @Transactional
    public RestaurantResponse register(RestaurantRequest request) {
        // 현재는 JWT 인증 미적용 상태라 임시로 1L 사용
        MemberEntity owner = memberRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        RestaurantEntity entity = RestaurantMapper.toEntity(request, owner);
        RestaurantEntity saved = restaurantRepository.save(entity);

        return RestaurantMapper.toResponse(saved);
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

    /** ✅ 지역별 조회 */
    public List<RestaurantResponse> getByArea(String area) {
        return restaurantRepository.findByArea(area)
                .stream()
                .map(RestaurantMapper::toResponse)
                .collect(Collectors.toList());
    }

    /** ✅ 카테고리별 조회 */
    public Page<RestaurantResponse> getByCategory(String category, PageRequest pageRequest) {
        return restaurantRepository.findByCategoryName(category, pageRequest)
                .map(RestaurantMapper::toResponse);
    }

    /** ✅ 이름 검색 */
    public List<RestaurantResponse> searchByName(String keyword) {
        return restaurantRepository.findByNameContaining(keyword)
                .stream()
                .map(RestaurantMapper::toResponse)
                .collect(Collectors.toList());
    }
}
