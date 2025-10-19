package kr.co.ync.projectA.domain.bookmark.controller;

import kr.co.ync.projectA.domain.bookmark.service.BookmarkService;
import kr.co.ync.projectA.domain.restaurant.dto.response.RestaurantResponse;
import kr.co.ync.projectA.global.common.dto.response.ResponseDTO;
import kr.co.ync.projectA.global.security.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    // ✅ 북마크 추가
    @PostMapping("/{restaurantId}")
    public ResponseEntity<ResponseDTO<Void>> add(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        bookmarkService.addBookmark(user.getMember(), restaurantId);
        return ResponseEntity.ok(new ResponseDTO<>(200, "북마크 추가 성공", null));
    }

    // ✅ 북마크 해제
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<ResponseDTO<Void>> delete(
            @PathVariable Long restaurantId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        bookmarkService.removeBookmark(user.getMember(), restaurantId);
        return ResponseEntity.ok(new ResponseDTO<>(200, "북마크 해제 성공", null));
    }

    // ✅ 내 북마크 목록
    @GetMapping("/me")
    public ResponseEntity<ResponseDTO<List<RestaurantResponse>>> myList(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        List<RestaurantResponse> list = bookmarkService.myList(user.getMember());
        return ResponseEntity.ok(new ResponseDTO<>(200, "목록 조회 성공", list));
    }
}
