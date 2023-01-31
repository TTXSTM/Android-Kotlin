package space.mairi.movieapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val idm: String = "",
    val title: String = "",
    val full_title: String = ""
)