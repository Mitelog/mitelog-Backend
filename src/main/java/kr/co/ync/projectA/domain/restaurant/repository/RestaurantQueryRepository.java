package kr.co.ync.projectA.domain.restaurant.repository;

import kr.co.ync.projectA.domain.restaurant.dto.request.RestaurantSearchRequest;
import kr.co.ync.projectA.domain.restaurant.dto.response.RestaurantResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantQueryRepository {
    Page<RestaurantResponse> search(RestaurantSearchRequest cond, Pageable pageable);
}
