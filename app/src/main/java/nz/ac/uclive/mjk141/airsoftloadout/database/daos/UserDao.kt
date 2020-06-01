package nz.ac.uclive.mjk141.airsoftloadout.database.daos

import androidx.room.*
import nz.ac.uclive.mjk141.airsoftloadout.database.tables.User

@Dao
interface UserDao: BaseDao<User> {

    @Query("SELECT * FROM User WHERE id = :key")
    fun selectById(key: Long): User?

    @Query("SELECT * FROM User WHERE email = :email")
    fun selectByEmail(email: String): User?

    @Query("SELECT id FROM User WHERE username = :username")
    fun selectIdByUsername(username: String): Long?

    @Query("SELECT id FROM User WHERE email = :email")
    fun selectIdByEmail(email: String): Long?

    @Query("SELECT id FROM User WHERE username = :username OR email = :email")
    fun selectIdsByUsernameOrEmail(username: String, email: String): List<Long>

}