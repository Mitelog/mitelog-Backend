package kr.co.ync.projectA.domain.restaurant.service;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.member.repository.MemberRepository;
import kr.co.ync.projectA.domain.restaurant.dto.Restaurant;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.domain.restaurant.mapper.RestaurantMapper;
import kr.co.ync.projectA.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;

    // 식당 등록
    public Restaurant register(Restaurant dto) {
        // 식당 등록자(owner) 조회
        MemberEntity owner = memberRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("등록자 회원을 찾을 수 없습니다."));

        // DTO → Entity
        RestaurantEntity entity = RestaurantMapper.toEntity(dto);
        entity = restaurantRepository.save(
                RestaurantEntity.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .address(entity.getAddress())
                        .area(entity.getArea())
                        .phone(entity.getPhone())
                        .image(entity.getImage())
                        .owner(owner) // 반드시 Owner 주입
                        .build()
        );

        return RestaurantMapper.toDTO(entity);
    }

    // 전체 조회
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll().stream()
                .map(RestaurantMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 상세 조회
    public Restaurant getById(Long id) {
        RestaurantEntity entity = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("식당을 찾을 수 없습니다."));
        return RestaurantMapper.toDTO(entity);
    }

    // 지역별 검색
    public List<Restaurant> getByArea(String area) {
        return restaurantRepository.findByArea(area).stream()
                .map(RestaurantMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 카테고리별 검색 (페이징)
    public Page<Restaurant> getByCategory(String category, Pageable pageable) {
        return restaurantRepository.findByCategory(category, pageable)
                .map(RestaurantMapper::toDTO);
    }

    // 이름 검색
    public List<Restaurant> searchByName(String keyword) {
        return restaurantRepository.findByNameContaining(keyword).stream()
                .map(RestaurantMapper::toDTO)
                .collect(Collectors.toList());
    }
}
