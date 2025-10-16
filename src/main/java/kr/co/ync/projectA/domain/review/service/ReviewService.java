package kr.co.ync.projectA.domain.review.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import kr.co.ync.projectA.domain.member.entity.MemberEntity;
import kr.co.ync.projectA.domain.member.repository.MemberRepository;
import kr.co.ync.projectA.domain.restaurant.entity.RestaurantEntity;
import kr.co.ync.projectA.domain.restaurant.repository.RestaurantRepository;
import kr.co.ync.projectA.domain.review.dto.request.ReviewRequest;
import kr.co.ync.projectA.domain.review.dto.response.ReviewResponse;
import kr.co.ync.projectA.domain.review.entity.ReviewEntity;
import kr.co.ync.projectA.domain.review.mapper.ReviewMapper;
import kr.co.ync.projectA.domain.review.repository.ReviewRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    /* ✅ 리뷰 등록 */
    public ReviewResponse createReview(String userEmail, ReviewRequest request) {
        MemberEntity member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        RestaurantEntity restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("가게 정보를 찾을 수 없습니다."));

        ReviewEntity review = ReviewMapper.toEntity(request, member, restaurant);
        ReviewEntity saved = reviewRepository.save(review);

        // ✅ 평균 평점 갱신
        updateRestaurantAverageRating(restaurant);

        return ReviewMapper.toResponse(saved);
    }

    /* ✅ 리뷰 수정 */
    public ReviewResponse updateReview(Long reviewId, String userEmail, ReviewRequest request) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        if (!review.getMember().getEmail().equals(userEmail)) {
            throw new SecurityException("본인만 리뷰를 수정할 수 있습니다.");
        }

        review = ReviewEntity.builder()
                .id(review.getId())
                .restaurant(review.getRestaurant())
                .member(review.getMember())
                .rating(request.getRating())
                .title(request.getTitle())
                .content(request.getContent())
                .image(request.getImage())
                .likeNum(review.getLikeNum())
                .build();

        ReviewEntity updated = reviewRepository.save(review);

        // ✅ 평균 평점 갱신
        updateRestaurantAverageRating(review.getRestaurant());

        return ReviewMapper.toResponse(updated);
    }

    /* ✅ 리뷰 삭제 */
    public void deleteReview(Long reviewId, String userEmail) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

        if (!review.getMember().getEmail().equals(userEmail)) {
            throw new SecurityException("본인만 리뷰를 삭제할 수 있습니다.");
        }

        reviewRepository.delete(review);

        // ✅ 평균 평점 갱신
        updateRestaurantAverageRating(review.getRestaurant());
    }

    /* ✅ 식당별 리뷰 조회 */
    public List<ReviewResponse> getReviewsByRestaurantId(Long restaurantId) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("가게 정보를 찾을 수 없습니다."));

        return reviewRepository.findByRestaurant(restaurant)
                .stream()
                .map(ReviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    /* ✅ 회원별 리뷰 조회 */
    public List<ReviewResponse> getReviewsByMemberId(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        return reviewRepository.findByMember(member)
                .stream()
                .map(ReviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    /* ✅ 평균 업데이트 로직 */
    private void updateRestaurantAverageRating(RestaurantEntity restaurant) {
        Double avg = reviewRepository.findAverageRatingByRestaurantId(restaurant.getId());
        restaurant.setAverageRating(avg != null ? avg : 0.0);
        restaurantRepository.save(restaurant);
    }

}
