package kr.co.ync.projectA.domain.bookmark.service;

import jakarta.persistence.EntityNotFoundException;
import kr.co.ync.projectA.domain.bookmark.entity.BookmarkEntity;
import kr.co.ync.projectA.domain.bookmark.repository.BookmarkRepository;
import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.restaurant.dto.response.RestaurantResponse;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final RestaurantRepository restaurantRepository;

    public void addBookmark(MemberEntity member, Long restaurantId) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("식당을 찾을 수 없습니다. id=" + restaurantId));

        if (bookmarkRepository.existsByMemberAndRestaurant(member, restaurant)) {
            throw new IllegalStateException("이미 북마크된 식당입니다.");
        }

        bookmarkRepository.save(BookmarkEntity.builder()
                .member(member)
                .restaurant(restaurant)
                .build());
    }

    public void removeBookmark(MemberEntity member, Long restaurantId) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("식당을 찾을 수 없습니다. id=" + restaurantId));

        BookmarkEntity bookmark = bookmarkRepository.findByMemberAndRestaurant(member, restaurant)
                .orElseThrow(() -> new EntityNotFoundException("북마크가 존재하지 않습니다."));

        bookmarkRepository.delete(bookmark);
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponse> myList(MemberEntity member) {
        return bookmarkRepository.findAllByMember(member).stream()
                .map(b -> toRestaurantResponse(b.getRestaurant()))
                .toList();
    }

    private RestaurantResponse toRestaurantResponse(RestaurantEntity r) {
        return RestaurantResponse.builder()
                .id(r.getId())
                .name(r.getName())
                .address(r.getAddress())
                .image(r.getImage())
                .averageRating(r.getAverageRating())
                .build();
    }
}
