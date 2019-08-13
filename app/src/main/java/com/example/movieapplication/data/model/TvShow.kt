package com.example.movieapplication.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

@Entity(
    tableName = "tv_shows"
)
data class TvShow(
    @Json(name = "id")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @Json(name = "name")
    @ColumnInfo(name = "name")
    var name: String?,

    @Json(name = "original_language")
    @ColumnInfo(name = "original_language")
    var language: String?,

    @Json(name = "first_air_date")
    @ColumnInfo(name = "first_air_date")
    var firstAirDate: String?,

    @Json(name = "overview")
    @ColumnInfo(name = "overview")
    var overview: String?,

    @Json(name = "vote_average")
    @ColumnInfo(name = "vote_average")
    var rating: Double?,

    @Json(name = "poster_path")
    @ColumnInfo(name = "poster_path")
    var image: String?
)

    : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(language)
        parcel.writeString(firstAirDate)
        parcel.writeString(overview)
        rating?.let { parcel.writeDouble(it) }
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TvShow> {
        override fun createFromParcel(parcel: Parcel): TvShow {
            return TvShow(parcel)
        }

        override fun newArray(size: Int): Array<TvShow?> {
            return arrayOfNulls(size)
        }
    }
}