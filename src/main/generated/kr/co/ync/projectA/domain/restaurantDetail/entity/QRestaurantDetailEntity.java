package kr.co.ync.projectA.domain.restaurantDetail.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRestaurantDetailEntity is a Querydsl query type for RestaurantDetailEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRestaurantDetailEntity extends EntityPathBase<RestaurantDetailEntity> {

    private static final long serialVersionUID = -1130312005L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRestaurantDetailEntity restaurantDetailEntity = new QRestaurantDetailEntity("restaurantDetailEntity");

    public final BooleanPath creditCard = createBoolean("creditCard");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath openHours = createString("openHours");

    public final BooleanPath parkingArea = createBoolean("parkingArea");

    public final BooleanPath privateRoom = createBoolean("privateRoom");

    public final kr.co.ync.projectA.domain.restaurant.entity.QRestaurantEntity restaurantId;

    public final BooleanPath smoking = createBoolean("smoking");

    public final BooleanPath unlimitDrink = createBoolean("unlimitDrink");

    public final BooleanPath unlimitFood = createBoolean("unlimitFood");

    public QRestaurantDetailEntity(String variable) {
        this(RestaurantDetailEntity.class, forVariable(variable), INITS);
    }

    public QRestaurantDetailEntity(Path<? extends RestaurantDetailEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRestaurantDetailEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRestaurantDetailEntity(PathMetadata metadata, PathInits inits) {
        this(RestaurantDetailEntity.class, metadata, inits);
    }

    public QRestaurantDetailEntity(Class<? extends RestaurantDetailEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.restaurantId = inits.isInitialized("restaurantId") ? new kr.co.ync.projectA.domain.restaurant.entity.QRestaurantEntity(forProperty("restaurantId"), inits.get("restaurantId")) : null;
    }

}

