package nz.ac.uclive.mjk141.airsoftloadout.database.tables

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity()
data class Equipment(
    @PrimaryKey
    var id: Long,
    var name: String,
    var imageId: String?
) {
    @Ignore
    var imageBitmap: Bitmap? = null
}