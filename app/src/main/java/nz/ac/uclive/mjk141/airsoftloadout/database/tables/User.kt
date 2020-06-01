package nz.ac.uclive.mjk141.airsoftloadout.database.tables

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [
    Index(value = ["email", "username"], unique = true)
])
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var email: String,
    var username: String,
    var password: String
)