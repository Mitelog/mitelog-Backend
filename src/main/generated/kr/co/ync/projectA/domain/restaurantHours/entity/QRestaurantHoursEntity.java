package kr.co.ync.projectA.domain.restaurantHours.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRestaurantHoursEntity is a Querydsl query type for RestaurantHoursEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRestaurantHoursEntity extends EntityPathBase<RestaurantHoursEntity> {

    private static final long serialVersionUID = 233770885L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRestaurantHoursEntity restaurantHoursEntity = new QRestaurantHoursEntity("restaurantHoursEntity");

    public final TimePath<java.time.LocalTime> closeTime = createTime("closeTime", java.time.LocalTime.class);

    public final StringPath dayOfWeek = createString("dayOfWeek");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isOpen = createBoolean("isOpen");

    public final TimePath<java.time.LocalTime> openTime = createTime("openTime", java.time.LocalTime.class);

    public final kr.co.ync.projectA.domain.restaurant.entity.QRestaurantEntity restaurantId;

    public QRestaurantHoursEntity(String variable) {
        this(RestaurantHoursEntity.class, forVariable(variable), INITS);
    }

    public QRestaurantHoursEntity(Path<? extends RestaurantHoursEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRestaurantHoursEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRestaurantHoursEntity(PathMetadata metadata, PathInits inits) {
        this(RestaurantHoursEntity.class, metadata, inits);
    }

    public QRestaurantHoursEntity(Class<? extends RestaurantHoursEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.restaurantId = inits.isInitialized("restaurantId") ? new kr.co.ync.projectA.domain.restaurant.entity.QRestaurantEntity(forProperty("restaurantId"), inits.get("restaurantId")) : null;
    }

}

