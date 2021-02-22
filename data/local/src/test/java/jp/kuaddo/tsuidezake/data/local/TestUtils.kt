package jp.kuaddo.tsuidezake.data.local

import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeEntity
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.SakeInfo
import jp.kuaddo.tsuidezake.data.local.internal.room.entity.WishUpdate
import jp.kuaddo.tsuidezake.data.local.internal.room.model.RoomSake

fun <T> Collection<T>.powerSet(): Set<Set<T>> =
    powerSet(this, setOf(emptySet()))

private tailrec fun <T> powerSet(left: Collection<T>, acc: Set<Set<T>>): Set<Set<T>> =
    if (left.isEmpty()) acc
    else powerSet(left.drop(1), acc + acc.map { it + left.first() })

internal val EMPTY_SAKE_INFO = SakeInfo(
    id = 0,
    name = "",
    description = null,
    region = "",
    brewer = null,
    imageUri = null,
    suitableTemperatures = emptySet(),
    goodFoodCategories = emptySet(),
)

internal val EMPTY_SAKE_ENTITY = SakeEntity(
    sakeInfo = EMPTY_SAKE_INFO,
    isAddedToWish = false,
    isAddedToTasted = false
)

internal val EMPTY_WISH_UPDATE = WishUpdate(
    sakeInfo = EMPTY_SAKE_INFO,
    isAddedToWish = false
)

internal val EMPTY_ROOM_SAKE = RoomSake(
    sakeEntity = EMPTY_SAKE_ENTITY,
    tagEntities = emptyList()
)
