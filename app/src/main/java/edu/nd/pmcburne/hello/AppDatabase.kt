package edu.nd.pmcburne.hello
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [Location::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    // abstract function that room will implement auto
    // provides access to the locationdao for db operations
    abstract fun locationDao(): LocationDao

    companion object{
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) { // return a single instance of the db
                Room.databaseBuilder(context, AppDatabase::class.java, "campus.db") // build db
                    .build().also { INSTANCE = it } // stores the created db instance
            }
    }
}