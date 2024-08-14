package my.id.jeremia.stressdetectionapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import my.id.jeremia.stressdetectionapp.database.dao.SensorDAO
import my.id.jeremia.stressdetectionapp.database.entity.Sensor

@Database(entities = [Sensor::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sensorDAO(): SensorDAO
}

