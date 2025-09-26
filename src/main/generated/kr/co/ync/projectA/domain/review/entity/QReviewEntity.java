package kr.co.ync.projectA.domain.review.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewEntity is a Querydsl query type for ReviewEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewEntity extends EntityPathBase<ReviewEntity> {

    private static final long serialVersionUID = -658208689L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewEntity reviewEntity = new QReviewEntity("reviewEntity");

    public final kr.co.ync.projectA.global.common.entity.QBaseTimeEntity _super = new kr.co.ync.projectA.global.common.entity.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDateTime = _super.createDateTime;

    public final kr.co.ync.projectA.domain.member.entity.QMemberEntity email;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    public final NumberPath<Long> likeNum = createNumber("likeNum", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDateTime = _super.modifiedDateTime;

    public final NumberPath<Long> rating = createNumber("rating", Long.class);

    public final kr.co.ync.projectA.domain.restaurant.entity.QRestaurantEntity restaurantId;

    public final StringPath title = createString("title");

    public QReviewEntity(String variable) {
        this(ReviewEntity.class, forVariable(variable), INITS);
    }

    public QReviewEntity(Path<? extends ReviewEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewEntity(PathMetadata metadata, PathInits inits) {
        this(ReviewEntity.class, metadata, inits);
    }

    public QReviewEntity(Class<? extends ReviewEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.email = inits.isInitialized("email") ? new kr.co.ync.projectA.domain.member.entity.QMemberEntity(forProperty("email")) : null;
        this.restaurantId = inits.isInitialized("restaurantId") ? new kr.co.ync.projectA.domain.restaurant.entity.QRestaurantEntity(forProperty("restaurantId"), inits.get("restaurantId")) : null;
    }

}

