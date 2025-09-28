package kr.co.ync.projectA.domain.bookmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kr.co.ync.projectA.domain.bookmark.entity.BookmarkEntitny;

public interface BookmarkRepository extends JpaRepository<BookmarkEntitny, Long> {
}
