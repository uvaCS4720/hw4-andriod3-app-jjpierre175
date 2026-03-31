package edu.nd.pmcburne.hello
import androidx.room.*
@Dao
interface LocationDao {
    @Query("SELECT * FROM locations") // gets all rows from the "locations" table
    suspend fun getAllLocations(): List<Location> // returns a list of location objects

    @Insert(onConflict = OnConflictStrategy.REPLACE) // inserts a list of location objects into the db
    suspend fun insertAll(locations: List<Location>) // row will be replaced with the new one if conflict

    @Query("SELECT DISTINCT tags FROM locations") // gets all unique tag values from the "locations" table
    suspend fun getAllTagStrings(): List<String> // returns a list of tag strings
}