package jp.kuaddo.tsuidezake.data.local.internal.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.kuaddo.tsuidezake.model.Tag

@Entity(tableName = TagEntity.TABLE_NAME)
internal data class TagEntity(
    @PrimaryKey @ColumnInfo(name = ColumnNames.ID)
    val id: Int,
    @ColumnInfo(name = ColumnNames.NAME)
    val name: String
) {
    fun toTag() = Tag(id = id, name = name)

    object ColumnNames {
        const val ID = "id"
        const val NAME = "name"
    }

    companion object {
        const val TABLE_NAME = "tags"

        fun of(tag: Tag) = TagEntity(id = tag.id, name = tag.name)
    }
}
