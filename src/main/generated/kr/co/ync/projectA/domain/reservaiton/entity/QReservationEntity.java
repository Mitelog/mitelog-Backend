package kr.co.ync.projectA.domain.reservaiton.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReservationEntity is a Querydsl query type for ReservationEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReservationEntity extends EntityPathBase<ReservationEntity> {

    private static final long serialVersionUID = -1996167089L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReservationEntity reservationEntity = new QReservationEntity("reservationEntity");

    public final kr.co.ync.projectA.global.common.entity.QBaseTimeEntity _super = new kr.co.ync.projectA.global.common.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDateTime = _super.createDateTime;

    public final kr.co.ync.projectA.domain.member.entity.QMemberEntity email;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDateTime = _super.modifiedDateTime;

    public final NumberPath<Long> numPeople = createNumber("numPeople", Long.class);

    public final kr.co.ync.projectA.domain.restaurant.entity.QRestaurantEntity restaurantId;

    public final DateTimePath<java.time.LocalDateTime> visit = createDateTime("visit", java.time.LocalDateTime.class);

    public QReservationEntity(String variable) {
        this(ReservationEntity.class, forVariable(variable), INITS);
    }

    public QReservationEntity(Path<? extends ReservationEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReservationEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReservationEntity(PathMetadata metadata, PathInits inits) {
        this(ReservationEntity.class, metadata, inits);
    }

    public QReservationEntity(Class<? extends ReservationEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.email = inits.isInitialized("email") ? new kr.co.ync.projectA.domain.member.entity.QMemberEntity(forProperty("email")) : null;
        this.restaurantId = inits.isInitialized("restaurantId") ? new kr.co.ync.projectA.domain.restaurant.entity.QRestaurantEntity(forProperty("restaurantId"), inits.get("restaurantId")) : null;
    }

}

