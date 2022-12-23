package space.mairi.movieapp.model

import android.os.ParcelFileDescriptor
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieInf(
    val movie: String,
    val descriptor : String,
    val min: Int,
    val genre: String,
    val budget: Int,
    val revenue: Int,
    val date: String,
) : Parcelable
