package kr.co.ync.projectA.domain.bookmark.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookmarkEntitny is a Querydsl query type for BookmarkEntitny
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookmarkEntitny extends EntityPathBase<BookmarkEntitny> {

    private static final long serialVersionUID = 79048249L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookmarkEntitny bookmarkEntitny = new QBookmarkEntitny("bookmarkEntitny");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final kr.co.ync.projectA.domain.member.entity.QMemberEntity member;

    public final kr.co.ync.projectA.domain.restaurant.entity.QRestaurantEntity restaurant;

    public QBookmarkEntitny(String variable) {
        this(BookmarkEntitny.class, forVariable(variable), INITS);
    }

    public QBookmarkEntitny(Path<? extends BookmarkEntitny> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookmarkEntitny(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookmarkEntitny(PathMetadata metadata, PathInits inits) {
        this(BookmarkEntitny.class, metadata, inits);
    }

    public QBookmarkEntitny(Class<? extends BookmarkEntitny> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new kr.co.ync.projectA.domain.member.entity.QMemberEntity(forProperty("member")) : null;
        this.restaurant = inits.isInitialized("restaurant") ? new kr.co.ync.projectA.domain.restaurant.entity.QRestaurantEntity(forProperty("restaurant"), inits.get("restaurant")) : null;
    }

}

