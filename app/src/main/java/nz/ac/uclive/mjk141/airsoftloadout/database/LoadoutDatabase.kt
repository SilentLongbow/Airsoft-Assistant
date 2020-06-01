package nz.ac.uclive.mjk141.airsoftloadout.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import nz.ac.uclive.mjk141.airsoftloadout.database.daos.UserDao
import nz.ac.uclive.mjk141.airsoftloadout.database.tables.User

@Database(entities = [User::class], version = 2)
abstract class LoadoutDatabase : RoomDatabase() {

    abstract val userDao: UserDao

    companion object {
        @Volatile
        private var INSTANCE: LoadoutDatabase? = null

        fun getInstance(context: Context): LoadoutDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LoadoutDatabase::class.java,
                        "airsoft_loadout_database.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}