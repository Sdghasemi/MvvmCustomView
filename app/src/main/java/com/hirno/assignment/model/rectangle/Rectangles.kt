package com.hirno.assignment.model.rectangle

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * The response model of rectangles endpoint
 */
@Parcelize
data class RectanglesResponseModel @JvmOverloads constructor(
    @SerializedName("rectangles")
    var rectangles: ArrayList<RectangleItemModel> = arrayListOf(),
) : Parcelable

/**
 * Model of each rectangle item
 */
@Parcelize
@Entity(tableName = "Rectangles")
data class RectangleItemModel @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: Int = 0,
    @ColumnInfo(name = "x")
    @SerializedName("x")
    var x: Float = 0f,
    @ColumnInfo(name = "y")
    @SerializedName("y")
    var y: Float = 0f,
    @ColumnInfo(name = "size")
    @SerializedName("size")
    var size: Float = 0f,
) : Parcelable