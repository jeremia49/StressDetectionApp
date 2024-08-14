package my.id.jeremia.stressdetectionapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import my.id.jeremia.stressdetectionapp.database.entity.Sensor

@Dao
interface SensorDAO {

    @Insert
    suspend fun insert(sensor: Sensor)

    @Query("SELECT * FROM sensors WHERE sesID = :sessionID")
    suspend fun getAllSensors(sessionID:String): List<Sensor>

    @Query("SELECT * FROM sensors")
    suspend fun getAllSensors(): List<Sensor>


    @Query("DELETE FROM sensors")
    suspend fun deleteAll()
}