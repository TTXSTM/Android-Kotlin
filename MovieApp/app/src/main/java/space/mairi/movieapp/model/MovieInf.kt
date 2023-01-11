package space.mairi.movieapp.model

import android.os.ParcelFileDescriptor
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieInf(
    val id : String,
    val movie: String,
) : Parcelable
