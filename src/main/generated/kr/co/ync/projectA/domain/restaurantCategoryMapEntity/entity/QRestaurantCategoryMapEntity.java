package kr.co.ync.projectA.domain.restaurantCategoryMapEntity.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRestaurantCategoryMapEntity is a Querydsl query type for RestaurantCategoryMapEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRestaurantCategoryMapEntity extends EntityPathBase<RestaurantCategoryMapEntity> {

    private static final long serialVersionUID = 850285890L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRestaurantCategoryMapEntity restaurantCategoryMapEntity = new QRestaurantCategoryMapEntity("restaurantCategoryMapEntity");

    public final kr.co.ync.projectA.domain.category.entity.QCategoryEntity category;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final kr.co.ync.projectA.domain.restaurant.entity.QRestaurantEntity restaurantId;

    public QRestaurantCategoryMapEntity(String variable) {
        this(RestaurantCategoryMapEntity.class, forVariable(variable), INITS);
    }

    public QRestaurantCategoryMapEntity(Path<? extends RestaurantCategoryMapEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRestaurantCategoryMapEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRestaurantCategoryMapEntity(PathMetadata metadata, PathInits inits) {
        this(RestaurantCategoryMapEntity.class, metadata, inits);
    }

    public QRestaurantCategoryMapEntity(Class<? extends RestaurantCategoryMapEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new kr.co.ync.projectA.domain.category.entity.QCategoryEntity(forProperty("category")) : null;
        this.restaurantId = inits.isInitialized("restaurantId") ? new kr.co.ync.projectA.domain.restaurant.entity.QRestaurantEntity(forProperty("restaurantId"), inits.get("restaurantId")) : null;
    }

}

