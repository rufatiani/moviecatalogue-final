package com.example.movieapplication.data.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class PageTvShow(
    @Json(name = "page")
    var id: Int,

    @Json(name = "total_results")
    var totalResults: Int,

    @Json(name = "total_pages")
    var totalPages: Int,

    @Json(name = "results")
    var results: List<TvShow>
) : Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(totalResults)
        parcel.writeInt(totalPages)
        parcel.writeList(results)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PageMovie> {
        override fun createFromParcel(parcel: Parcel): PageMovie {
            return PageMovie(parcel)
        }

        override fun newArray(size: Int): Array<PageMovie?> {
            return arrayOfNulls(size)
        }
    }
}