package kr.co.ync.projectA.domain.restaurant.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRestaurantEntity is a Querydsl query type for RestaurantEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRestaurantEntity extends EntityPathBase<RestaurantEntity> {

    private static final long serialVersionUID = -1720860711L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRestaurantEntity restaurantEntity = new QRestaurantEntity("restaurantEntity");

    public final kr.co.ync.projectA.global.common.entity.QBaseTimeEntity _super = new kr.co.ync.projectA.global.common.entity.QBaseTimeEntity(this);

    public final StringPath address = createString("address");

    public final StringPath area = createString("area");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDateTime = _super.createDateTime;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath image = createString("image");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDateTime = _super.modifiedDateTime;

    public final StringPath name = createString("name");

    public final kr.co.ync.projectA.domain.member.entity.QMemberEntity owner;

    public final StringPath phone = createString("phone");

    public QRestaurantEntity(String variable) {
        this(RestaurantEntity.class, forVariable(variable), INITS);
    }

    public QRestaurantEntity(Path<? extends RestaurantEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRestaurantEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRestaurantEntity(PathMetadata metadata, PathInits inits) {
        this(RestaurantEntity.class, metadata, inits);
    }

    public QRestaurantEntity(Class<? extends RestaurantEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.owner = inits.isInitialized("owner") ? new kr.co.ync.projectA.domain.member.entity.QMemberEntity(forProperty("owner")) : null;
    }

}

