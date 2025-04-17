package com.udayrana.roamto_uday

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlaceDao {
    @Insert
    suspend fun insert(vararg places: Place)

    @Update
    suspend fun update(vararg places: Place)

    @Delete
    suspend fun delete(vararg places: Place)

    @Query("SELECT * FROM place")
    suspend fun getAll(): List<Place>

    @Query("SELECT count(uid) FROM place")
    suspend fun getCount(): Int
}