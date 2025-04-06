package com.bizsolutions.dealmate.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDate
import java.time.LocalTime

@Database(entities = [
    ClientEntity::class,
    TaskEntity::class,
    EventEntity::class,
    CallEntity::class,
    DealEntity::class],
    version = 1, exportSchema = false)
@TypeConverters(MyTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao
    abstract fun eventDao(): EventDao
    abstract fun taskDao(): TaskDao
    abstract fun callDao(): CallDao
    abstract fun dealDao(): DealDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

private class MyTypeConverters {
    @TypeConverter
    fun localDateFromTimestamp(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(value) }
    }

    @TypeConverter
    fun localDateToTimestamp(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    @TypeConverter
    fun localTimeFromTimestamp(value: Int?): LocalTime? {
        return value?.let { LocalTime.ofSecondOfDay(value.toLong()) }
    }

    @TypeConverter
    fun localTimeToTimestamp(time: LocalTime?): Int? {
        return time?.toSecondOfDay()
    }
}