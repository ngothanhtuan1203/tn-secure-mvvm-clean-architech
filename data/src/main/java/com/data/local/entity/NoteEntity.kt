package com.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull

@Parcelize
@Entity(tableName = "notes")
data class NoteEntity(
    @ColumnInfo(name = "title")
    var canonicalTitle: String,
    @ColumnInfo(name = "detail")
    var canonicalDetail: String
) : Parcelable {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}