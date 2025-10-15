package kr.co.ync.projectA.domain.bookmark.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import kr.co.ync.projectA.domain.bookmark.entity.BookmarkEntity;

import java.util.stream.IntStream;

@SpringBootTest
class BookmarkRepositoryTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Test
    void testInsertBookmark() {
        IntStream.rangeClosed(1, 30)
                .forEach(i -> {
                    String email = "user" + i + "@aaa.com";
                    BookmarkEntity bookmarkEntity = BookmarkEntity
                            .builder()
//                            .restaurantId()
                            .build();

                    bookmarkRepository.save(bookmarkEntity);
                });
    }
}