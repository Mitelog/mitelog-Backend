package kr.co.ync.projectA.domain.restaurantHoildays.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRestaurantHolidaysEntity is a Querydsl query type for RestaurantHolidaysEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRestaurantHolidaysEntity extends EntityPathBase<RestaurantHolidaysEntity> {

    private static final long serialVersionUID = 191970485L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRestaurantHolidaysEntity restaurantHolidaysEntity = new QRestaurantHolidaysEntity("restaurantHolidaysEntity");

    public final TimePath<java.time.LocalTime> closeTime = createTime("closeTime", java.time.LocalTime.class);

    public final DatePath<java.time.LocalDate> holidayDate = createDate("holidayDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isOpen = createBoolean("isOpen");

    public final StringPath note = createString("note");

    public final TimePath<java.time.LocalTime> openTime = createTime("openTime", java.time.LocalTime.class);

    public final kr.co.ync.projectA.domain.restaurant.entity.QRestaurantEntity restaurantId;

    public QRestaurantHolidaysEntity(String variable) {
        this(RestaurantHolidaysEntity.class, forVariable(variable), INITS);
    }

    public QRestaurantHolidaysEntity(Path<? extends RestaurantHolidaysEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRestaurantHolidaysEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRestaurantHolidaysEntity(PathMetadata metadata, PathInits inits) {
        this(RestaurantHolidaysEntity.class, metadata, inits);
    }

    public QRestaurantHolidaysEntity(Class<? extends RestaurantHolidaysEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.restaurantId = inits.isInitialized("restaurantId") ? new kr.co.ync.projectA.domain.restaurant.entity.QRestaurantEntity(forProperty("restaurantId"), inits.get("restaurantId")) : null;
    }

}

