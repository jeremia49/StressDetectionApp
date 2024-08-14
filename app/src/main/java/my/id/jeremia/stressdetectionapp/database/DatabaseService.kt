package my.id.jeremia.stressdetectionapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import my.id.jeremia.stressdetectionapp.database.dao.SensorDAO
import my.id.jeremia.stressdetectionapp.database.entity.Sensor
import javax.inject.Singleton

@Singleton
@Database(
    entities = [
        Sensor::class,
    ],
    exportSchema = false,
    version = 1,
)
abstract class DatabaseService : RoomDatabase() {

    abstract fun sensorDAO(): SensorDAO
}