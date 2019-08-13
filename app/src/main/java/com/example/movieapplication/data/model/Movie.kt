package com.example.movieapplication.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json


@Entity(
    tableName = "movies"
)
data class Movie(
    @Json(name = "id")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @Json(name = "title")
    @ColumnInfo(name = "title")
    var title: String?,

    @Json(name = "original_language")
    @ColumnInfo(name = "language")
    var language: String?,

    @Json(name = "release_date")
    @ColumnInfo(name = "release_date")
    var releaseDate: String?,

    @Json(name = "overview")
    @ColumnInfo(name = "overview")
    var overview: String?,

    @Json(name = "vote_average")
    @ColumnInfo(name = "vote_average")
    var rating: Double?,

    @Json(name = "poster_path")
    @ColumnInfo(name = "poster_path")
    var image: String?
) : Parcelable {

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
        parcel.writeString(title)
        parcel.writeString(language)
        parcel.writeString(releaseDate)
        parcel.writeString(overview)
        rating?.let { parcel.writeDouble(it) }
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}