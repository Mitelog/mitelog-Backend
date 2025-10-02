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

    public Restaurant register(Restaurant dto) {
        MemberEntity owner = memberRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("등록자 회원을 찾을 수 없습니다."));
        RestaurantEntity entity = RestaurantMapper.toEntity(dto);
        entity = restaurantRepository.save(
                RestaurantEntity.builder()
                        .id(entity.getId())
                        .name(entity.getName())
                        .address(entity.getAddress())
                        .area(entity.getArea())
                        .phone(entity.getPhone())
                        .image(entity.getImage())
                        .owner(owner)
                        .build()
        );
        return RestaurantMapper.toDTO(entity);
    }

    public List<Restaurant> getAll() {
        return restaurantRepository.findAll().stream()
                .map(RestaurantMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Restaurant getById(Long id) {
        RestaurantEntity entity = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("식당을 찾을 수 없습니다."));
        return RestaurantMapper.toDTO(entity);
    }

//    public List<Restaurant> getByArea(String area) {
//        return restaurantRepository.findByArea(area).stream()
//                .map(RestaurantMapper::toDTO)
//                .collect(Collectors.toList());
//    }
//
//    public Page<Restaurant> getByCategory(String category, Pageable pageable) {
//        return restaurantRepository.findByCategory(category, pageable)
//                .map(RestaurantMapper::toDTO);
//    }
//
//    public List<Restaurant> searchByName(String keyword) {
//        return restaurantRepository.findByNameContaining(keyword).stream()
//                .map(RestaurantMapper::toDTO)
//                .collect(Collectors.toList());
//    }
}
