package space.mairi.movieapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class ItemDTO(
    val title : String?,
    val fullTitle : String?,
    val year : String?,
    val releaseDate : String?,
    val runtimeMins : String?,
    val plot : String?,
    val imDbRating : String?
)