package com.example.moviefavoritemodule.data.model

import android.os.Parcel
import android.os.Parcelable

data class Movie(
    val id: Int,
    var title: String?,
    var language: String?,
    var releaseDate: String?,
    var overview: String?,
    var rating: Double?,
    var image: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(language)
        parcel.writeString(releaseDate)
        parcel.writeString(overview)
        parcel.writeValue(rating)
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