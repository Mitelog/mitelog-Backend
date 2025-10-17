package kr.co.ync.projectA.domain.bookmark.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookmarkEntity is a Querydsl query type for BookmarkEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookmarkEntity extends EntityPathBase<BookmarkEntity> {

    private static final long serialVersionUID = 418191947L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookmarkEntity bookmarkEntity = new QBookmarkEntity("bookmarkEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final kr.co.ync.projectA.domain.member.entity.QMemberEntity member;

    public final kr.co.ync.projectA.domain.restaurant.entity.QRestaurantEntity restaurant;

    public QBookmarkEntity(String variable) {
        this(BookmarkEntity.class, forVariable(variable), INITS);
    }

    public QBookmarkEntity(Path<? extends BookmarkEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookmarkEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookmarkEntity(PathMetadata metadata, PathInits inits) {
        this(BookmarkEntity.class, metadata, inits);
    }

    public QBookmarkEntity(Class<? extends BookmarkEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new kr.co.ync.projectA.domain.member.entity.QMemberEntity(forProperty("member")) : null;
        this.restaurant = inits.isInitialized("restaurant") ? new kr.co.ync.projectA.domain.restaurant.entity.QRestaurantEntity(forProperty("restaurant"), inits.get("restaurant")) : null;
    }

}

