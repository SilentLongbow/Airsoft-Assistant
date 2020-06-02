package nz.ac.uclive.mjk141.airsoftloadout.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import nz.ac.uclive.mjk141.airsoftloadout.database.tables.Equipment

@Dao
interface EquipmentDao: BaseDao<Equipment> {

    @Query("SELECT * FROM Equipment")
    fun selectAll(): LiveData<Equipment>
}