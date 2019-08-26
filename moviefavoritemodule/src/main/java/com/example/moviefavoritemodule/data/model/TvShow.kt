package com.example.moviefavoritemodule.data.model

import android.os.Parcel
import android.os.Parcelable

data class TvShow(
    val id: Int,
    var name: String?,
    var language: String?,
    var firstAirDate: String?,
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
        parcel.writeString(name)
        parcel.writeString(language)
        parcel.writeString(firstAirDate)
        parcel.writeString(overview)
        parcel.writeValue(rating)
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